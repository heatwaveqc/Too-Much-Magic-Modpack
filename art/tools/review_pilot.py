#!/usr/bin/env python3
"""Small original-art-only review board; previews are not game lighting evidence."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageEnhance
from prepare_pilot import PATHS, PACK

FONT='/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
def font(n):return ImageFont.truetype(FONT,n)
def paste(sheet,im,xy):sheet.paste(im,xy,im if im.mode=='RGBA' else None)

def main():
    sheet=Image.new('RGB',(1200,1210),'#171d24');d=ImageDraw.Draw(sheet)
    d.text((26,18),'TOO MUCH MAGIC HD',font=font(28),fill='#eef0e9')
    d.text((26,55),'PILOT 01  /  original 64px art  /  owner review pending',font=font(17),fill='#a7b5bf')
    order=['diamond','emerald','iron_ingot','gold_ingot','stone','cobblestone','oak_log','oak_log_top',
           'oak_planks','livingrock','pure_daisy','rune_mana','mana_diamond','runic_altar_side','iron_layer_1','button']
    tiles={'stone','cobblestone','oak_log','oak_log_top','oak_planks','livingrock'}
    for i,name in enumerate(order):
        x=20+(i%4)*295;y=95+(i//4)*270
        d.rounded_rectangle((x,y,x+280,y+255),radius=8,fill='#252e37')
        d.text((x+12,y+10),name.replace('_',' ').title(),font=font(17),fill='#ecede5')
        im=Image.open(PACK/'assets'/PATHS[name]).convert('RGBA')
        if name in tiles:
            paste(sheet,im.resize((128,128),Image.Resampling.NEAREST),(x+12,y+42))
            tiled=Image.new('RGBA',(192,192))
            for yy in range(3):
                for xx in range(3):tiled.paste(im,(xx*64,yy*64))
            paste(sheet,tiled.resize((111,111),Image.Resampling.NEAREST),(x+155,y+42))
            d.text((x+160,y+158),'3 x 3 repeat',font=font(12),fill='#a7b5bf')
            paste(sheet,im,(x+12,y+180));d.text((x+85,y+200),'64px native',font=font(12),fill='#a7b5bf')
        elif name=='iron_layer_1':
            paste(sheet,im,(x+12,y+45))
            d.text((x+12,y+182),'256 x 128 / 4x UV layout',font=font(12),fill='#a7b5bf')
            d.text((x+12,y+205),'Renderer check pending',font=font(12),fill='#dabf89')
        elif name=='button':
            # Logical UI size, not the 4x source resolution.
            logical=im.resize((200,20),Image.Resampling.LANCZOS)
            paste(sheet,logical,(x+35,y+60));d.text((x+83,y+61),'Button',font=font(14),fill='white')
            d.text((x+12,y+111),'800 x 80 / nine-slice retained',font=font(12),fill='#a7b5bf')
            d.text((x+12,y+142),'No baked text in source PNG',font=font(12),fill='#a7b5bf')
            d.text((x+12,y+205),'Renderer check pending',font=font(12),fill='#dabf89')
        else:
            d.rectangle((x+12,y+42,x+144,y+174),fill='#15191e')
            d.rectangle((x+151,y+42,x+267,y+174),fill='#d6d1c4')
            paste(sheet,im.resize((128,128),Image.Resampling.NEAREST),(x+14,y+44))
            paste(sheet,im,(x+178,y+75))
            for size,xx in [(64,12),(32,101),(16,164)]:
                paste(sheet,im.resize((size,size),Image.Resampling.LANCZOS),(x+xx,y+182))
                d.text((x+xx,y+235),str(size)+'px',font=font(10),fill='#a7b5bf')
            # Simulated dark viewing swatch, explicitly not a runtime test.
            paste(sheet,ImageEnhance.Brightness(im).enhance(.28).resize((32,32)),(x+224,y+186))
            d.text((x+214,y+235),'dim*',font=font(10),fill='#a7b5bf')
    d.text((24,1180),'*Dim swatches are image-only simulations. No client, world, torchlight or darkness-mod test performed.',font=font(13),fill='#a7b5bf')
    out=Path('art/reviews/pilot-01');out.mkdir(parents=True,exist_ok=True)
    sheet.save(out/'comparison.png',optimize=True)

if __name__=='__main__':main()
