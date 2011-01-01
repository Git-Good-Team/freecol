/**
 *  Copyright (C) 2002-2011  The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */


package net.sf.freecol.client.gui.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.TradeRoute;
import net.sf.freecol.common.model.Unit;

import net.miginfocom.swing.MigLayout;


/**
 * Allows the user to edit trade routes.
 */
public final class TradeRouteDialog extends FreeColDialog<TradeRoute> implements ActionListener {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TradeRouteDialog.class.getName());
    
    private static enum Action { OK, CANCEL, DEASSIGN, DELETE }

    private final JButton ok = new JButton(Messages.message("ok"));
    private final JButton cancel = new JButton(Messages.message("cancel"));

    private final JButton editRouteButton = new JButton(Messages.message("traderouteDialog.editRoute"));
    private final JButton newRouteButton = new JButton(Messages.message("traderouteDialog.newRoute"));
    private final JButton removeRouteButton = new JButton(Messages.message("traderouteDialog.removeRoute"));
    private final JButton deassignRouteButton = new JButton(Messages.message("traderouteDialog.deassignRoute"));

    private final DefaultListModel listModel = new DefaultListModel();
    private final JList tradeRoutes = new JList(listModel);
    private final JScrollPane tradeRouteView = new JScrollPane(tradeRoutes);

    /**
     * The constructor that will add the items to this panel.
     * @param parent The parent of this panel.
     */
    public TradeRouteDialog(final Canvas parent, TradeRoute selectedRoute) {

        super(parent);

        ok.setActionCommand(Action.OK.toString());
        ok.addActionListener(this);
        enterPressesWhenFocused(ok);

        cancel.setActionCommand(Action.CANCEL.toString());
        cancel.addActionListener(this);
        enterPressesWhenFocused(cancel);
        setCancelComponent(cancel);

        deassignRouteButton.addActionListener(this);
        deassignRouteButton.setToolTipText(Messages.message("traderouteDialog.deassign.tooltip"));
        deassignRouteButton.setActionCommand(Action.DEASSIGN.toString());
        enterPressesWhenFocused(deassignRouteButton);

        tradeRoutes.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    updateButtons();
                }
            });

        // button for adding new TradeRoute
        newRouteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Player player = getMyPlayer();
                    TradeRoute newRoute = getController().getNewTradeRoute(player);
                    newRoute.setName(Messages.message("traderouteDialog.newRoute"));
                    if (parent.showFreeColDialog(new TradeRouteInputDialog(parent, newRoute))) {
                        listModel.addElement(newRoute);
                    }
                }
            });

        // button for editing TradeRoute
        editRouteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    parent.showFreeColDialog(new TradeRouteInputDialog(parent, 
                        (TradeRoute) tradeRoutes.getSelectedValue()));
                }
            });

        // button for deleting TradeRoute
        removeRouteButton.addActionListener(this);
        removeRouteButton.setActionCommand(Action.DELETE.toString());

        Player player = getMyPlayer();

        List<TradeRoute> theRoutes = new ArrayList<TradeRoute>(player.getTradeRoutes());
        player.resetTradeRouteCounts();
        Collections.sort(theRoutes, tradeRouteComparator);
        for (TradeRoute route : theRoutes) {
            listModel.addElement(route);
        }

        tradeRoutes.setCellRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList list,
                                                              Object value,
                                                              int index,
                                                              boolean selected,
                                                              boolean focus) {
                    Component ret = super.getListCellRendererComponent(list,
                        value, index, selected, focus);
                    TradeRoute tradeRoute = (TradeRoute) value;
                    String name = tradeRoute.getName();
                    int n = tradeRoute.getCount();

                    if (n > 0) {
                        setText(name + "  (" + String.valueOf(n) + ")");
                    } else {
                        setText(name);
                    }
                    return ret;
                }
            });

        if (selectedRoute != null) {
            tradeRoutes.setSelectedValue(selectedRoute, true);
        }
        updateButtons();

        setLayout(new MigLayout("wrap 2", "", ""));

        add(getDefaultHeader(Messages.message("traderouteDialog.name")),
            "span, align center");
  
        add(tradeRouteView, "height 360:400");
        add(newRouteButton, "split 3, flowy, growx");
        add(editRouteButton, "growx");
        add(removeRouteButton, "growx");

        add(ok, "newline 20, span, split 3, tag ok");
        add(cancel, "tag cancel");
        add(deassignRouteButton);

        setSize(getPreferredSize());

    }
    
     private static final Comparator<TradeRoute> tradeRouteComparator = new Comparator<TradeRoute>() {
         public int compare(TradeRoute r1, TradeRoute r2) {
             return r1.getName().compareTo(r2.getName());
         }
     };

    public void requestFocus() {
        ok.requestFocus();
    }

    public void updateButtons() {
        if (tradeRoutes.getSelectedIndex() == -1) {
            editRouteButton.setEnabled(false);
            removeRouteButton.setEnabled(false);
            deassignRouteButton.setEnabled(false);
        } else {
            editRouteButton.setEnabled(true);
            removeRouteButton.setEnabled(true);
            deassignRouteButton.setEnabled(true);
        }
    }

    
    /**
     * This function analyses an event and calls the right methods to take
     * care of the user's requests.
     *
     * @param event The incoming ActionEvent.
     */
    public void actionPerformed(ActionEvent event) {
        Action action = Enum.valueOf(Action.class, event.getActionCommand());
        switch (action) {
        case OK:
            getCanvas().remove(this);
            ArrayList<TradeRoute> routes = new ArrayList<TradeRoute>();
            for (int index = 0; index < listModel.getSize(); index++) {
                routes.add((TradeRoute) listModel.getElementAt(index));
            }
            setResponse((TradeRoute) tradeRoutes.getSelectedValue());
            break;
        case CANCEL:
            getCanvas().remove(this);
            setResponse(null);
            break;
        case DEASSIGN: case DELETE:
            TradeRoute route = (TradeRoute) tradeRoutes.getSelectedValue();
            if (route != null) {
                for (Unit unit : route.getAssignedUnits()) {
                    getController().clearOrders(unit);
                }
            }
            if (action == Action.DEASSIGN) {
                getCanvas().remove(this);
                setResponse(TradeRoute.NO_TRADE_ROUTE);
                return;
            }
            if (route != null) {
                listModel.removeElementAt(tradeRoutes.getSelectedIndex());
                Player player = getMyPlayer();
                player.getTradeRoutes().remove(route);
                getController().setTradeRoutes(player.getTradeRoutes());
            }
            break;
        }        
    }
}
