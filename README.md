# Joacarpet

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)

A small [carpet](https://github.com/gnembon/fabric-carpet) extension mod.

## Carpet Mod Settings
### insaneBehaviors
Makes the random velocities of droppers and projectiles (as well as both the position and velocity of blocks broken by pistons) systematically iterate through the most extreme values possible, and then repeatedly iterate through all the halfway points in between, in a sense attempting every point in a 3d/5d "grid" that slowly increases in resolution.

For droppers and projectiles, this setting determines whether the max value corresponds to the old gaussian randomness limits (\"extreme\"), or the limits of the triangular randomness introduced in 1.19 (\"sensible\"). Both settings function the same for blocks being broken by pistons.

For the `/insanebehaviors <reset/getstate/setstate>` command, see `/carpet commandInsaneBehaviors`.

Do note that insaneBehaviors works on a global iterator: any triggering event will step through an iteration from all other insaneBehaviors events, too.

* Type: `String`
* Categories: `CREATIVE`, `JOA`
* Options: `extreme`, `sensible`, `off`
* Default value: `off`

### commandInsaneBehaviors
The command used for the `insaneBehaviors` rule.
\"reset\" sets the `resolution` and `counter` back to the default values. \"getstate\" and \"setstate\" are used to manually read and write the current iteration state.

* Type: `String`
* Categories: `COMMAND`, `CREATIVE`, `JOA`
* Options `true`, `ops`, `false`, `0`, `1`, `2`, `3`, `4`
* Default value: `ops`

### blockTickling
Lets you send manual block and/or shape updates to blocks using a feather item. Shape updates are sent from the block in front of the face you're clicking on. Useful if you're working with update interactions off or with budded blocks.

* Type: `String`
* Categories: `CREATIVE`, `JOA`
* Options `off`, `blockupdates`, `shapeupdates`, `both`
* Default value: `off`