"""Type-A contract for the Getting Started first-magic chapter (no Minecraft boot)."""
from __future__ import annotations

import hashlib
import importlib.util
import json
import math
from pathlib import Path
import re
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
CHAPTER = ROOT / "config/ftbquests/quests/chapters/intro.snbt"
LANG = ROOT / "config/ftbquests/quests/lang/en_us.snbt"
FIRST_MAGIC = "Pick Your First Magic"
READABLE_MAIN = (
    "Punch a Tree", "Make a Crafting Table", FIRST_MAGIC,
    "Find an Everyday Magical Shortcut", "Try a First Spell or Ritual",
    "The Rest of the Pack Is Yours",
)
SURVIVAL = (
    "Make a Wooden Pickaxe", "Gather Stone", "Upgrade to Stone",
    "Find Your First Iron", "Iron Pickaxe",
)
BROWSE_SIDE = (
    "A Place for Your Things", "Light the Furnace", "Begin a Library",
    "Build a Bookshelf", "A Little Protection", "Take Your Magic Into the World",
)
ROOT_CHOICES = (
    "Welcome to Too Much Magic", "Choose Your Magic Controls",
    "I'm Ready for Regular Mode", "Punch a Tree",
)

def load(path: str) -> dict:
    return json.loads((ROOT / path).read_text(encoding="utf-8"))

def orient(a, b, c):
    return ((b[0]-a[0])*(c[1]-a[1])-(b[1]-a[1])*(c[0]-a[0]))

def crosses(a,b,c,d):
    return orient(a,b,c)*orient(a,b,d)<-1e-7 and orient(c,d,a)*orient(c,d,b)<-1e-7

def distance_to_segment(p, a, b):
    dx,dy=b[0]-a[0],b[1]-a[1]
    k=max(0.,min(1.,((p[0]-a[0])*dx+(p[1]-a[1])*dy)/(dx*dx+dy*dy)))
    return math.hypot(p[0]-a[0]-k*dx,p[1]-a[1]-k*dy)

def segment_hits_rect(a,b,x,y,w,h):
    """Liang-Barsky clipping detects art-panel/arrow overlaps."""
    dx,dy=b[0]-a[0],b[1]-a[1]
    lo,hi=0.,1.
    for p,q in ((-dx,a[0]-(x-w/2)), (dx,(x+w/2)-a[0]),
                (-dy,a[1]-(y-h/2)), (dy,(y+h/2)-a[1])):
        if abs(p)<1e-12:
            if q<0: return False
            continue
        z=q/p
        if p<0: lo=max(lo,z)
        else: hi=min(hi,z)
        if lo>hi: return False
    return True

class GettingStartedA(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.authored=[x for x in load("manifest/quest_content.json")["quests"]
                      if x["chapter"]=="Getting Started"]
        cls.resolved=[x for x in load("manifest/quest_content_resolved.json")["quests"]
                      if x["chapter"]=="Getting Started"]
        cls.by_title={x["title"]:x for x in cls.resolved}
        cls.authored_by_title={x["title"]:x for x in cls.authored}
        cls.text=CHAPTER.read_text(encoding="utf-8")
        cls.blocks={}
        for match in re.finditer(r'\n\t\t\{\n\t\t\tid: "([0-9A-F]{16})"[\s\S]*?\n\t\t\}',cls.text):
            cls.blocks[match.group(1)]=match.group(0)
        cls.layout=load("manifest/quest_layouts.json")
        cls.positions={x["quest_id"]:tuple(x["position"]) for x in cls.layout["positions"]
                       if x["chapter"]=="Getting Started"}

    def test_stable_quest_ids_tasks_rewards_and_no_duplicate_starter_items(self):
        self.assertEqual(len(self.authored),len(self.resolved))
        self.assertEqual(len(self.resolved),len(self.blocks))
        self.assertEqual(len(self.resolved),len(self.positions))
        self.assertEqual(len(self.resolved),20)
        self.assertEqual(len({x["quest_id"] for x in self.resolved}),20)
        self.assertEqual(len({x["task_id"] for x in self.resolved}),20)
        self.assertEqual(self.by_title["Welcome to Too Much Magic"]["quest_id"],
                         "544D301000100001")
        self.assertEqual(self.by_title[FIRST_MAGIC]["quest_id"],
                         "544D501000D00000")
        self.assertEqual(self.by_title["I'm Ready for Regular Mode"]["quest_id"],
                         "544D309000100001")
        for q in self.resolved:
            b=self.blocks[q["quest_id"]]
            for key in ("task_id","xp_reward_id"):
                self.assertIn(q[key],b)
            if "item_reward_id" in q:self.assertIn(q["item_reward_id"],b)
            self.assertIn(f'xp: {q["reward"]["xp"]}',b)
            self.assertEqual(q["optional"],self.authored_by_title[q["title"]]["optional"])
        welcome=self.blocks[self.by_title["Welcome to Too Much Magic"]["quest_id"]]
        self.assertIn("enigmaticlegacyplus:unwitnessed_amulet",welcome)
        self.assertIn("enigmaticlegacyplus:cursed_ring",welcome)
        self.assertNotIn("akashictome:tome",self.text)
        self.assertNotIn("minecraft:cooked_beef",self.text)
        self.assertNotIn("comforts:sleeping_bag_white",self.text)
        self.assertIn('team_reward: false',self.blocks[self.by_title["I'm Ready for Regular Mode"]["quest_id"]])

    def test_first_magic_independent_of_vanilla_iron_and_graduation(self):
        self.assertEqual(tuple(x["title"] for x in self.resolved if x["depends"] is None),
                         ROOT_CHOICES)
        for child,parent in zip(READABLE_MAIN[1:],READABLE_MAIN):
            self.assertEqual(self.by_title[child]["depends"],parent,child)
        self.assertEqual(self.by_title[FIRST_MAGIC]["depends"],"Make a Crafting Table")
        self.assertEqual(self.by_title["Take Your Magic Into the World"]["depends"],FIRST_MAGIC)
        for title in SURVIVAL+("A Place for Your Things","Light the Furnace","Begin a Library"):
            self.assertTrue(self.by_title[title]["optional"],title)
        self.assertEqual(self.by_title["A Little Protection"]["depends"],"Iron Pickaxe")
        self.assertIsNone(self.by_title["I'm Ready for Regular Mode"]["depends"])
        self.assertTrue(self.by_title["I'm Ready for Regular Mode"]["optional"])
        ancestors=set()
        stack=[self.by_title["The Rest of the Pack Is Yours"]["depends"]]
        while stack:
            step=stack.pop()
            if step and step not in ancestors:
                ancestors.add(step)
                stack.append(self.by_title[step]["depends"])
        self.assertFalse(ancestors.intersection(set(SURVIVAL)|{"I'm Ready for Regular Mode"}))

    def test_ftb_dependencies_icons_positions_and_localizations(self):
        loc=LANG.read_text(encoding="utf-8")
        for q in self.resolved:
            b=self.blocks[q["quest_id"]]
            p=q["depends"]
            if p:
                pid=self.by_title[p]["quest_id"]
                self.assertIn('dependencies: ["'+pid+'"]',b)
            else:
                self.assertNotIn("dependencies:",b)
            xy=self.positions[q["quest_id"]]
            self.assertIn(f"x: {xy[0]:.1f}d",b)
            self.assertIn(f"y: {xy[1]:.1f}d",b)
            a=self.authored_by_title[q["title"]]
            self.assertIn('quest.'+q["quest_id"]+'.title: '+json.dumps(q["title"],ensure_ascii=False),loc)
            self.assertIn('quest.'+q["quest_id"]+'.quest_desc: ['+
                          json.dumps(a["description"],ensure_ascii=False)+']',loc)
        self.assertIn('shape: "hexagon"',self.blocks[self.by_title[FIRST_MAGIC]["quest_id"]])
        self.assertIn('shape: "octagon"',self.blocks[self.by_title["I'm Ready for Regular Mode"]["quest_id"]])
        src=importlib.util.spec_from_file_location("build_quest_ui",ROOT/"scripts/rebuild_quest_ui.py")
        self.assertIsNotNone(src)
        gen=importlib.util.module_from_spec(src)
        src.loader.exec_module(gen)
        wanted=gen.layout()["Getting Started"]
        self.assertEqual({q["title"]:self.positions[q["quest_id"]] for q in self.resolved},
                         {k:tuple(v) for k,v in wanted.items()})

    def test_no_visual_crossings_node_strikes_or_decorative_overlaps(self):
        p={q["title"]:self.positions[q["quest_id"]] for q in self.resolved}
        edges=[(q["depends"],q["title"]) for q in self.resolved if q["depends"]]
        # Straight connector lines must not cross or run through unrelated icons.
        for i,(a,b) in enumerate(edges):
            start,end=p[a],p[b]
            self.assertLessEqual(math.dist(start,end),13.0,(a,b))
            self.assertGreaterEqual(end[0],start[0],(a,b))
            for k,xy in p.items():
                if k not in (a,b):
                    self.assertGreaterEqual(distance_to_segment(xy,start,end),1.6,(a,b,k))
            for c,d in edges[i+1:]:
                if len({a,b,c,d})==4:
                    self.assertFalse(crosses(start,end,p[c],p[d]),(a,b,c,d))
        for i,(k,xy) in enumerate(p.items()):
            for k2,xy2 in list(p.items())[i+1:]:
                self.assertGreaterEqual(math.dist(xy,xy2),3.2,(k,k2))
        artwork=re.findall(r'image: "([^"]+)", x: (-?[\d.]+)d, y: (-?[\d.]+)d, width: ([\d.]+)d, height: ([\d.]+)d',self.text)
        self.assertEqual(len(artwork),3)
        for img,x,y,w,h in artwork:
            self.assertTrue(img.startswith("minecraft:textures/item/"))
            x,y,w,h=map(float,(x,y,w,h))
            for k,(nx,ny) in p.items():
                self.assertFalse(abs(nx-x)<(w+1.7)/2+0.2 and abs(ny-y)<(h+1.7)/2+0.2,(img,k))
            for a,b in edges:
                self.assertFalse(segment_hits_rect(p[a],p[b],x,y,w,h),(img,a,b))

    def test_packwiz_exact_hashes(self):
        pack=tomllib.loads((ROOT/"pack.toml").read_text(encoding="utf-8"))
        index_bytes=(ROOT/"index.toml").read_bytes()
        self.assertEqual(pack["index"]["hash"],hashlib.sha256(index_bytes).hexdigest())
        ix=tomllib.loads(index_bytes.decode("utf-8"))
        selected={row["file"]:row["hash"] for row in ix["files"]}
        for path in ("config/ftbquests/quests/chapters/intro.snbt",
                     "config/ftbquests/quests/lang/en_us.snbt"):
            self.assertEqual(selected[path],hashlib.sha256((ROOT/path).read_bytes()).hexdigest())

if __name__=="__main__":
    unittest.main()
