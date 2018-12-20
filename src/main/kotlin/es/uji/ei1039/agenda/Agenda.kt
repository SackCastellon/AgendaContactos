package es.uji.ei1039.agenda

import es.uji.ei1039.agenda.data.DatabaseManager
import es.uji.ei1039.agenda.util.Directories
import es.uji.ei1039.agenda.util.KoinContainer
import es.uji.ei1039.agenda.view.RootLayout
import tornadofx.App
import tornadofx.FX
import tornadofx.launch

class Agenda : App(RootLayout::class) {
    override fun init() {
        FX.dicontainer = KoinContainer
        Directories.create()
        DatabaseManager


    }


}

fun main(args: Array<String>): Unit = launch<Agenda>(args)
