#!/usr/bin/env python3
"""Thun's Structures — Hobbit Hole donor (upstream 1.1.0)
Copyright (c) 2025 Thun (https://modrinth.com/user/thun)
SPDX-License-Identifier: MIT

Attribution above reconstructed from creator profile and release publication date;
the supplied archive has no embedded copyright/license notice. The exact
release page identifies the project as MIT-licensed:
https://modrinth.com/datapack/thuns-structures/version/1.1.0
Project ZLL4Dkxb, release J9wwIhgC (October 5, 2025).
License checked 2026-11-19 against the official release page and
https://opensource.org/license/mit. No separate asset restriction located.
The upstream API was inaccessible, so archive authenticity is supported by
matching filename, size and the earlier locally recorded SHA256, not a newly
verified upstream API hash.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

Unmodified research-only source member. Do not place in a live game.
Archive: Thuns-structures-1-1-0.zip
Archive SHA256: 90387a1b75917566fb6b97ade20cbbe828c9d9733272d4f4fc62949e7446d14c
Member: data/thunstructures/structure/hobbithole.nbt
Original DataVersion 4440; 13 x 8 x 15; 397 nonair.
Includes uncleaned loot references and entities. Renovation/normalization pending.
No worldgen or other donor buildings are included.
"""
import base64
import hashlib
from pathlib import Path

PAYLOAD = "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"
SHA256 = "82fd7d21e21460fd29dc2a7c43e6875a572c62199ecd78d82ea6f2278d41c0c6"

def extract():
    data = base64.b64decode(PAYLOAD, validate=True)
    if len(data) != 3023 or hashlib.sha256(data).hexdigest() != SHA256:
        raise ValueError("Original donor checksum mismatch")
    target = Path(__file__).with_name("hobbithole.original.nbt")
    if target.exists() and target.read_bytes() != data:
        raise FileExistsError("Refusing to overwrite a different donor")
    target.write_bytes(data)
    target.with_suffix(".nbt.LICENSE.txt").write_text(__doc__.split("Unmodified research-only")[0].strip()+"\n",encoding="utf-8")
    return target

if __name__ == "__main__":
    print(extract())
