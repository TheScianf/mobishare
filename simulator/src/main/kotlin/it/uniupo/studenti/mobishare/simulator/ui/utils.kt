package it.uniupo.studenti.mobishare.simulator.ui

import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.simulator.entity.Dock
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class VehicleCellRenderer: ListCellRenderer<Vehicle> {
    override fun getListCellRendererComponent(
        list: JList<out Vehicle?>?,
        value: Vehicle?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component? {
        if (value == null) return null

        val label = JLabel(value.id.toString())
        if(isSelected || cellHasFocus) {
            label.background = list?.selectionBackground
            label.foreground = list?.selectionForeground
        } else {
            label.background = list?.background
            label.foreground = list?.foreground
        }

        return label
    }

}

class DockCellRenderer: ListCellRenderer<Dock> {
    override fun getListCellRendererComponent(
        list: JList<out Dock?>?,
        value: Dock?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component? {
        if (value == null) return null

        val label = JLabel("Dock n.${value.number} - Park: ${value.park}")
        if(isSelected || cellHasFocus) {
            label.background = list?.selectionBackground
            label.foreground = list?.selectionForeground
        } else {
            label.background = list?.background
            label.foreground = list?.foreground
        }

        return label
    }

}