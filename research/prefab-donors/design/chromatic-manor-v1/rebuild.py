#!/usr/bin/env python3
import gzip,hashlib,pathlib,subprocess,sys
p=pathlib.Path(__file__).resolve().parent
src=gzip.decompress((p/'build_manor.py.gz').read_bytes())
assert hashlib.sha256(src).hexdigest()=='5b92122a1cb6ad25a9a4ad4bb67f2b4faf865e70b77fcd6375f1411b82fe942f', 'Source hash mismatch'
f=p/'build_manor.py';f.write_bytes(src)
subprocess.run([sys.executable,str(f)],check=True)
nbt=p/'prefab-extension'/'chromatic_manor.nbt'
expected='5d260834ea95a8d2982473ee61d61d90996a101844794ec065443b28b254fb2f'
actual=hashlib.sha256(nbt.read_bytes()).hexdigest()
if actual!=expected: print('Compressed NBT differs across gzip implementations; inspect audit:',actual);sys.exit(1)
print('Exact V1 NBT verified:',nbt)
