package es.uji.ei1039.agenda

import es.uji.ei1039.agenda.util.KoinContainer
import es.uji.ei1039.agenda.view.RootLayout
import tornadofx.App
import tornadofx.FX
import tornadofx.launch

class Agenda : App(RootLayout::class)

fun main(args: Array<String>) {
    // Setup dependency injection framework to work with TornadoFX
    FX.dicontainer = KoinContainer
    launch<Agenda>(args)
}
