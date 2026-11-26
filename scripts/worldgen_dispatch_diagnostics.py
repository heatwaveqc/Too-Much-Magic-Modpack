"""Read-only registration diagnostics and bounded dispatch error classification."""
import json
import re
import urllib.error
import urllib.request
import qa_dispatch_bridge as qa

WORKFLOW_ID=430805133
WORKFLOW_PATH=".github/workflows/worldgen-audit-pilot.yml"
DISPATCH="actions/workflows/worldgen-audit-pilot.yml/dispatches"

def classify(raw):
    """Never print arbitrary response values, URLs, headers, credentials, or input values."""
    try:
        data=json.loads(raw)
        if not isinstance(data,dict): return "unrecognized response"
        message=data.get("message","")
        if not isinstance(message,str): return "unrecognized response"
        m=message.lower()
        categories=[
            ("unexpected inputs",r"unexpected inputs?|inputs?.*not (?:defined|recognized|expected)"),
            ("missing required inputs",r"required inputs?|missing inputs?"),
            ("invalid input value",r"invalid input|invalid value|invalid choice"),
            ("workflow dispatch trigger missing",r"workflow_dispatch.*(?:not|missing)|does not have.*workflow_dispatch"),
            ("workflow disabled",r"workflow.*(?:disabled|inactive)"),
            ("workflow invalid",r"invalid workflow|workflow.*(?:syntax|validation|invalid|not valid)"),
            ("ref unavailable",r"no ref found|ref.*(?:not found|does not exist|invalid)|branch.*not found"),
            ("validation failed",r"validation failed"),
        ]
        results=[label for label,pattern in categories if re.search(pattern,m)]
        # Only explicitly allowlisted field names from structured errors.
        fields=set()
        codes=set()
        errors=data.get("errors",[])
        for e in errors if isinstance(errors,list) else []:
            if not isinstance(e,dict): continue
            if e.get("field") in ("ref","inputs","authorized_build","expected_launcher_sha","request_nonce","workflow"):
                fields.add(e["field"])
            if e.get("code") in ("missing","missing_field","invalid","already_exists","custom","unprocessable"):
                codes.add(e["code"])
        return "; ".join((results or ["unrecognized response"])+
                        ["field="+f for f in sorted(fields)]+["code="+c for c in sorted(codes)])
    except (ValueError,TypeError,UnicodeError):
        return "unrecognized response"

class DiagnosticAPI(qa.API):
    def __call__(self,path,method="GET",body=None):
        if method!="POST" or path!=DISPATCH:
            return super().__call__(path,method,body)
        # Match canonical API headers and request exactly; add only safe error classification.
        req=urllib.request.Request(f"https://api.github.com/repos/{qa.REPO}/{path}",
            data=json.dumps(body).encode(),method=method,headers={
                "Authorization":"Bearer "+self.token,"Accept":"application/vnd.github+json",
                "X-GitHub-Api-Version":"2022-11-28","User-Agent":"TMM-QA-Dispatch-Bridge",
                "Content-Type":"application/json"})
        try:
            with urllib.request.urlopen(req,timeout=30) as res:
                raw=res.read()
                return json.loads(raw) if raw else {}
        except urllib.error.HTTPError as e:
            try: category=classify(e.read(16384))
            except OSError: category="response unavailable"
            raise qa.Refused(f"GitHub API POST returned HTTP {e.code} for {DISPATCH}; classification: {category}") from None

def registration(api):
    """Inspect registered workflow without writing refs or dispatching anything."""
    evidence={}
    for label,key in (("filename","worldgen-audit-pilot.yml"),("numeric",str(WORKFLOW_ID))):
        try:
            row=api("actions/workflows/"+key)
            evidence[label]={"id_matches":row.get("id")==WORKFLOW_ID,
                "path_matches":row.get("path")==WORKFLOW_PATH,
                "state":row.get("state") if row.get("state") in
                ("active","disabled_manually","disabled_inactivity","disabled_fork","deleted") else "unknown"}
        except qa.Refused:
            evidence[label]={"lookup":"failed"}
    head=api("git/ref/heads/main")["object"]["sha"]
    if not re.fullmatch(r"[0-9a-f]{40}",head): raise qa.Refused("Invalid main SHA")
    evidence["main_sha"]=head
    return evidence
