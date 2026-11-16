// Canonical silver equipment without TeamLapen: Eidolon tools AND armor.
// No free extra anti-lycan effects; a separate Hunter skill may add one later.
ServerEvents.recipes(event => {
  for (const slot of ['sword', 'pickaxe', 'axe', 'shovel', 'hoe']) {
    event.remove({ id: 'embers:silver_' + slot })
  }
  console.info('[TMM silver] Retained five Eidolon tools + four Eidolon armor recipes; removed five ordinary Embers duplicates')
})
