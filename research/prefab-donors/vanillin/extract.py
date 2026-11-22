#!/usr/bin/env python3
"""Extract exact owner-supplied raw tower and license; no runtime deployment."""
import hashlib, pathlib, sys, zipfile
src = pathlib.Path(sys.argv[1]); dest = pathlib.Path(sys.argv[2])
assert hashlib.sha256(src.read_bytes()).hexdigest() == '5b3befa6c61471edc42a587556c734857f3d06812b8d2460a0a08170efba1e92', 'Wrong donor release'
dest.mkdir(parents=True, exist_ok=True)
with zipfile.ZipFile(src) as z:
    assert z.testzip() is None
    raw = z.read('data/vanillinstructures/structures/wizardtower.nbt')
    assert hashlib.sha256(raw).hexdigest() == '3f87b751e40912ca958ea9957a3a6430e68c38248274c2f6bfff69109cbd0ecc'
    (dest / 'wizardtower.nbt').write_bytes(raw)
    for name in ('LICENSE_VanillinStructures', 'fabric.mod.json'):
        (dest / name).write_bytes(z.read(name))
