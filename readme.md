# Fire Emblem Character Creator v3

Created originally by TheFlyingMinotaur! (v1)

Art resources provided by Iscaneus!

Updated by BaconMaster120! (v2)

Converted to Scala and updated again by Vale the Violet Mote (v3)

Using AK's Skin Palettes and Blade's FE (hair) Palettes!

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

v3 Bugs Found:
- Sliders don't have fine-tuned control (you can click on the number labels for increments but it's still helpful if we had a number field. Perhaps also add arrows to the labels around the 0 to coax people into clicking on them naturally. )
- Portrait border cuts off pixels
- "Anti-Alias" doesn't account for scaling & rotation
- "Anti-Alias" overwrites pixels it shouldn't (eyebrows and other parts of the face - AA should only be touching skin, and light skin at that!)
- Lack of control over AA's two colors used