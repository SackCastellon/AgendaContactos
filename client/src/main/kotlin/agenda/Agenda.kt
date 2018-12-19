package agenda

import agenda.util.KoinContainer
import agenda.view.RootLayout
import tornadofx.App
import tornadofx.FX
import tornadofx.launch

class Agenda : App(RootLayout::class)

fun main(args: Array<String>) {
    // Setup dependency injection framework to work with TornadoFX
    FX.dicontainer = KoinContainer
    launch<Agenda>(args)
}
