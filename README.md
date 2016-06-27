###MineReset - An multi-threaded minecraft plugin
This was supposed to be a multi-threaded mine plugin that was supposed to not cause that much lag due
to the fact that it would separate the block updates in several ticks.

However, once I realized that Spigot didn't allow me to change the blocks from another thread, I had
to jury-rig way of making it work on the main server thread.