"""Parse installed KubeJS files with the pinned Rhino JAR, without starting Minecraft."""
import argparse,concurrent.futures,json,subprocess
from pathlib import Path

def main():
    p=argparse.ArgumentParser()
    p.add_argument('--minecraft',type=Path,required=True);p.add_argument('--java',type=Path,required=True)
    p.add_argument('--rhino-jar',type=Path,required=True);p.add_argument('--report',type=Path,required=True)
    a=p.parse_args();helper=Path(__file__).resolve().parents[1]/'tests/ParseQaSurveys.java'
    files=sorted(path for directory in ['server_scripts','client_scripts','startup_scripts'] for path in (a.minecraft/'kubejs'/directory).rglob('*.js'))
    def check(path):
        cmd=[str(a.java),'-Xmx128m','-cp',str(a.rhino_jar),str(helper),str(path)]
        try:
            r=subprocess.run(cmd,capture_output=True,text=True,encoding='utf-8',errors='replace',timeout=45)
            return {'file':path.relative_to(a.minecraft).as_posix(),'exit':r.returncode,
                'detail':(r.stdout+r.stderr).replace(str(a.minecraft),'<minecraft>').replace(str(helper),'<parser-helper>')[:1800]}
        except subprocess.TimeoutExpired:return {'file':path.relative_to(a.minecraft).as_posix(),'exit':None,'detail':'Parser timed out'}
    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool: rows=list(pool.map(check,files))
    result={'scope':'SYNTAX_ONLY_NOT_EXECUTION','rhino':a.rhino_jar.name,'checked':len(rows),'failed':sum(r['exit']!=0 for r in rows),'results':rows}
    a.report.parent.mkdir(parents=True,exist_ok=True);a.report.write_text(json.dumps(result,indent=2)+'\n',encoding='utf-8')
    print(json.dumps({k:v for k,v in result.items() if k!='results'}));print(json.dumps([r['file'] for r in rows if r['exit']!=0]))
    return 1 if result['failed'] else 0
if __name__=='__main__': raise SystemExit(main())
