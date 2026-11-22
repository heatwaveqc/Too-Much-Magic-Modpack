#!/usr/bin/env python3
"""Create original TMM Theurgy chapter artwork (five RGBA PNGs, stdlib only).

These are original geometric illustrations: no copied ATM10 questpic texture.
Generation is deliberately explicit and reproducible. Assets are STAGED until the
quest integration writer packages them and updates the packwiz hash index.
"""
from __future__ import annotations

import math
import struct
import zlib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "docs/quest-pilots/theurgy/generated/assets"
SIZE = {"title.png": (384, 104), "vessels.png": (240, 92),
        "niter.png": (240, 92), "reformation.png": (240, 92),
        "wires.png": (240, 92)}


def png_chunk(kind: bytes, data: bytes) -> bytes:
    return struct.pack(">I", len(data)) + kind + data + struct.pack(
        ">I", zlib.crc32(kind + data) & 0xFFFFFFFF)


class Panel:
    def __init__(self, w: int, h: int):
        self.w, self.h = w, h
        self.data = bytearray(w * h * 4)
        for y in range(h):
            for x in range(w):
                glow = 0.18 + 0.25 * max(0.0, 1.0 - abs(x / w - 0.52) * 2)
                rgb = (int(14 + 16 * glow), int(24 + 18 * glow), int(33 + 24 * glow))
                self.pixel(x, y, (*rgb, 215))

    def pixel(self, x: int, y: int, rgba: tuple[int, int, int, int]) -> None:
        if 0 <= x < self.w and 0 <= y < self.h:
            i = 4 * (y * self.w + x)
            old = tuple(self.data[i + k] for k in range(4))
            a = rgba[3] / 255
            for k in range(3):
                self.data[i + k] = min(255, int(old[k] * (1 - a) + rgba[k] * a))
            self.data[i + 3] = min(255, old[3] + int((255 - old[3]) * a))

    def disk(self, cx: float, cy: float, radius: float, color: tuple[int, int, int, int]):
        left, right = max(0, int(cx - radius - 1)), min(self.w, int(cx + radius + 2))
        top, bottom = max(0, int(cy - radius - 1)), min(self.h, int(cy + radius + 2))
        r2 = radius * radius
        for y in range(top, bottom):
            for x in range(left, right):
                if (x - cx) ** 2 + (y - cy) ** 2 <= r2:
                    self.pixel(x, y, color)

    def ring(self, cx: float, cy: float, radius: float, color, thickness=1.5):
        for i in range(int(radius * 12) + 16):
            angle = i * math.tau / (int(radius * 12) + 16)
            self.disk(cx + radius * math.cos(angle), cy + radius * math.sin(angle),
                      thickness, color)

    def line(self, x0, y0, x1, y1, color, thick=1.2):
        steps = max(1, int(math.hypot(x1 - x0, y1 - y0) * 2))
        for s in range(steps + 1):
            t = s / steps
            self.disk(x0 * (1 - t) + x1 * t, y0 * (1 - t) + y1 * t,
                      thick, color)

    def border(self):
        gold = (173, 124, 63, 210)
        for inset in (4, 8):
            for a, b in [((inset, inset), (self.w - inset - 1, inset)),
                         ((inset, self.h - inset - 1), (self.w - inset - 1, self.h - inset - 1))]:
                self.line(*a, *b, gold, 0.7)
            self.line(inset, inset, inset, self.h - inset, gold, 0.7)
            self.line(self.w - inset - 1, inset, self.w - inset - 1,
                      self.h - inset, gold, 0.7)

    def save(self, path: Path):
        raw = bytearray()
        for y in range(self.h):
            raw.append(0)
            raw.extend(self.data[y * self.w * 4: (y + 1) * self.w * 4])
        hdr = struct.pack(">IIBBBBB", self.w, self.h, 8, 6, 0, 0, 0)
        out = (b"\\x89PNG\\r\\n\\x1a\\n" + png_chunk(b"IHDR", hdr)
               + png_chunk(b"IDAT", zlib.compress(bytes(raw), 9))
               + png_chunk(b"IEND", b""))
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(out)


GOLD = (224, 176, 94, 235)
COPPER = (191, 112, 75, 240)
MERCURY = (95, 217, 212, 230)
BLUE = (82, 131, 209, 185)
WHITE = (229, 221, 189, 190)


def vessel(panel: Panel, x, y, scale, color):
    panel.line(x, y - 22 * scale, x, y - 12 * scale, GOLD, 1.7 * scale)
    panel.line(x - 6 * scale, y - 12 * scale, x + 6 * scale, y - 12 * scale,
               WHITE, 1.2 * scale)
    panel.line(x - 6 * scale, y - 12 * scale, x - 14 * scale, y + 11 * scale,
               GOLD, 1.5 * scale)
    panel.line(x + 6 * scale, y - 12 * scale, x + 14 * scale, y + 11 * scale,
               GOLD, 1.5 * scale)
    panel.line(x - 14 * scale, y + 11 * scale, x + 14 * scale, y + 11 * scale,
               GOLD, 1.5 * scale)
    panel.disk(x, y + 3 * scale, 8 * scale, (*color[:3], 125))
    panel.ring(x, y + 3 * scale, 8 * scale, color, .9 * scale)


def draw(name: str, w: int, h: int) -> Panel:
    p = Panel(w, h)
    p.border()
    cx, cy = w / 2, h / 2
    if name == "title.png":
        p.ring(cx, cy, h * .36, GOLD, 1.5)
        p.ring(cx, cy, h * .23, MERCURY, 1.0)
        for theta in range(6):
            angle = math.tau * theta / 6
            p.disk(cx + math.cos(angle) * h * .34,
                   cy + math.sin(angle) * h * .34, 3, COPPER)
        for dx, col in [(-30, MERCURY), (0, GOLD), (30, COPPER)]:
            vessel(p, cx + dx, cy + 9, .75, col)
        p.line(24, cy, 88, cy, GOLD, 1.4)
        p.line(w - 88, cy, w - 24, cy, GOLD, 1.4)
        p.ring(48, cy, 12, COPPER, 1.2)
        p.ring(w - 48, cy, 12, MERCURY, 1.2)
    elif name == "vessels.png":
        for x, color in [(w * .26, MERCURY), (w * .5, GOLD), (w * .74, COPPER)]:
            vessel(p, x, cy + 8, 1.15, color)
        p.line(w * .32, cy - 18, w * .44, cy - 18, GOLD, 1.5)
        p.line(w * .56, cy - 18, w * .68, cy - 18, GOLD, 1.5)
    elif name == "niter.png":
        for j in range(4):
            x = 45 + j * (w - 90) / 3
            radius = 8 + j * 4.5
            for k in range(6):
                a = math.tau * k / 6
                b = math.tau * (k + 1) / 6
                p.line(x + radius * math.cos(a), cy + radius * math.sin(a),
                       x + radius * math.cos(b), cy + radius * math.sin(b),
                       [COPPER, GOLD, MERCURY, WHITE][j], 1.5)
            p.disk(x, cy, 3.3, [COPPER, GOLD, MERCURY, WHITE][j])
            if j < 3:
                p.line(x + radius + 5, cy, x + (w - 90) / 3 - radius - 5,
                       cy, GOLD, 1)
    elif name == "reformation.png":
        for x, color in [(w * .22, COPPER), (w * .5, MERCURY), (w * .78, GOLD)]:
            p.line(x - 15, cy + 18, x + 15, cy + 18, color, 2)
            p.line(x - 10, cy - 1, x - 10, cy + 18, color, 2)
            p.line(x + 10, cy - 1, x + 10, cy + 18, color, 2)
            p.ring(x, cy - 10, 10, color, 1.3)
        p.line(w * .22 + 10, cy - 10, w * .5 - 10, cy - 10, MERCURY, 1.8)
        p.line(w * .5 + 10, cy - 10, w * .78 - 10, cy - 10, MERCURY, 1.8)
        p.ring(cx, cy, 32, BLUE, 1)
    elif name == "wires.png":
        for y in [cy - 18, cy, cy + 18]:
            p.line(25, y, w - 25, y, COPPER, 1.5)
        for i, x in enumerate(range(45, w - 20, 42)):
            p.ring(x, cy + (18 if i % 2 else -18), 6, MERCURY, 1.8)
            p.disk(x, cy, 4, GOLD)
        p.ring(cx, cy, 27, BLUE, 0.8)
    return p


def main():
    for name, (w, h) in SIZE.items():
        target = OUT / name
        draw(name, w, h).save(target)
        print(f"{target.relative_to(ROOT)} ({w}x{h})")


if __name__ == "__main__":
    main()
