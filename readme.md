# Fire Emblem Character Creator v3

Created originally by TheFlyingMinotaur! (v1)

Art resources provided by Iscaneus!

Updated by BaconMaster120! (v2)

Converted to Scala and updated again by Vale the Violet Mote (v3)

Using Blade's FE Hair & Skin Palettes!

v3 Changelog:
- Rewritten in Scala 
- Piece Scaling
- Piece Rotation
- Piece Offsets reworked to always cover full area
- Piece Dropdowns reworked to show just the necessary info, and sorted alphabetically
- Piece Select-By-Preview rights-side panel
- Piece Border Color Selection
- Prefab color panels (with unique one for skin)
- Full display of palettes' shades
- Full control over each shade in a palette
- Prefab Palettes (again with unique for skin)
- "Anti-Aliasing" of where the hair meets the face
- Unique colors in use counter
- Export of GBA compatible size png
- Saving & Loading


v3.1.0 Changelog: 
- Critical Bug Fixed: Closing the window doesn't kill java instance (causes many instances of java to lurk each time you close - please check your task manager to kill lingering instances)
- Bug Fixed: Save/Load not working on old java versions (commonly found installed on Windows)

v3 Bugs Found / Features desired:
- [FTR] Sliders don't have fine-tuned control (you can click on the number labels for increments but it's still helpful if we had a number field. Perhaps also add arrows to the labels around the 0 to coax people into clicking on them naturally. )
- [FTR] Lack of control over AA's two colors used
- [FTR] Randomization buttons like ivycase's fork (https://github.com/ivycase/CharacterCreatorRelease)
- [BUG] Anti-Alias looks upward and sideward for shading which is not how shading actually works. (Need to make it look only downwards in every dir)


v3.0.x Changelog:
- Bug Fixed: "Anti-Alias" doesn't account for scaling & rotation
- Bug Fixed: "Anti-Alias" overwrites pixels it shouldn't (eyebrows and other parts of the face - AA should only be touching skin, and light skin at that!)
- Bug Fixed: Portrait border cuts off pixels
- Bug Fixed: Large token images (e.g. FalcoKnight) throw an out of bounds error.
- Bug Fixed: Offset hair & face combos not working correctly with Anti-Alias.
- Feature Added: Save/Load a character edit for further working/tweaking.
- Bug Fixed: Out of bounds on anti-aliasing (rare cases)
- Feature Added: 382040 as the default outline (fe8)
- Feature Added: F8F8C0 F8D070 E89850 987048 604848 (ephraim skin) as a new palette for skin & as the default skin
- Note, the colors are thanks to Rawr776.