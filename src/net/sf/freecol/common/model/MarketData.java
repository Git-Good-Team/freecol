/**
 *  Copyright (C) 2002-2007  The FreeCol Team
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


package net.sf.freecol.common.model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

//import net.sf.freecol.FreeCol;

/**
 * Objects of this class hold the market data for a particular type of
 * good.
 */
public class MarketData extends FreeColObject {

    /**
     * Describe costToBuy here.
     */
    private int costToBuy;

    /**
     * Describe paidForSale here.
     */
    private int paidForSale;

    /**
     * Describe amountInMarket here.
     */
    private int amountInMarket;

    /**
     * Describe oldPrice here.
     */
    private int oldPrice;

    /**
     * Describe arrears here.
     */
    private int arrears;

    /**
     * Describe sales here.
     */
    private int sales;

    /**
     * Describe incomeBeforeTaxes here.
     */
    private int incomeBeforeTaxes;

    /**
     * Describe incomeAfterTaxes here.
     */
    private int incomeAfterTaxes;

    /**
     * Package constructor: This class is only supposed to be constructed
     * by {@link Market}.
     * 
     * @param game The game this object should be created within.
     */
    public MarketData() {}
    
    public MarketData(GoodsType goodsType) {
        setId(goodsType.getId());
    }

    /**
     * Get the <code>CostToBuy</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getCostToBuy() {
        return costToBuy;
    }

    /**
     * Set the <code>CostToBuy</code> value.
     *
     * @param newCostToBuy The new CostToBuy value.
     */
    public final void setCostToBuy(final int newCostToBuy) {
        this.costToBuy = newCostToBuy;
    }

    /**
     * Get the <code>PaidForSale</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getPaidForSale() {
        return paidForSale;
    }

    /**
     * Set the <code>PaidForSale</code> value.
     *
     * @param newPaidForSale The new PaidForSale value.
     */
    public final void setPaidForSale(final int newPaidForSale) {
        this.paidForSale = newPaidForSale;
    }

    /**
     * Get the <code>AmountInMarket</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getAmountInMarket() {
        return amountInMarket;
    }

    /**
     * Set the <code>AmountInMarket</code> value.
     *
     * @param newAmountInMarket The new AmountInMarket value.
     */
    public final void setAmountInMarket(final int newAmountInMarket) {
        this.amountInMarket = newAmountInMarket;
    }

    /**
     * Get the <code>OldPrice</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getOldPrice() {
        return oldPrice;
    }

    /**
     * Set the <code>OldPrice</code> value.
     *
     * @param newOldPrice The new OldPrice value.
     */
    public final void setOldPrice(final int newOldPrice) {
        this.oldPrice = newOldPrice;
    }

    /**
     * Get the <code>Arrears</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getArrears() {
        return arrears;
    }

    /**
     * Set the <code>Arrears</code> value.
     *
     * @param newArrears The new Arrears value.
     */
    public final void setArrears(final int newArrears) {
        this.arrears = newArrears;
    }

    /**
     * Get the <code>Sales</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getSales() {
        return sales;
    }

    /**
     * Set the <code>Sales</code> value.
     *
     * @param newSales The new Sales value.
     */
    public final void setSales(final int newSales) {
        this.sales = newSales;
    }

    /**
     * Get the <code>IncomeBeforeTaxes</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getIncomeBeforeTaxes() {
        return incomeBeforeTaxes;
    }

    /**
     * Set the <code>IncomeBeforeTaxes</code> value.
     *
     * @param newIncomeBeforeTaxes The new IncomeBeforeTaxes value.
     */
    public final void setIncomeBeforeTaxes(final int newIncomeBeforeTaxes) {
        this.incomeBeforeTaxes = newIncomeBeforeTaxes;
    }

    /**
     * Get the <code>IncomeAfterTaxes</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getIncomeAfterTaxes() {
        return incomeAfterTaxes;
    }

    /**
     * Set the <code>IncomeAfterTaxes</code> value.
     *
     * @param newIncomeAfterTaxes The new IncomeAfterTaxes value.
     */
    public final void setIncomeAfterTaxes(final int newIncomeAfterTaxes) {
        this.incomeAfterTaxes = newIncomeAfterTaxes;
    }

    /**
     * This method writes an XML-representation of this object to
     * the given stream.
     * 
     * <br><br>
     * 
     * Only attributes visible to the given <code>Player</code> will 
     * be added to that representation if <code>showAll</code> is
     * set to <code>false</code>.
     *  
     * @param out The target stream.
     * @param player The <code>Player</code> this XML-representation 
     *      should be made for, or <code>null</code> if
     *      <code>showAll == true</code>.
     * @param showAll Only attributes visible to <code>player</code> 
     *      will be added to the representation if <code>showAll</code>
     *      is set to <i>false</i>.
     * @param toSavedGame If <code>true</code> then information that
     *      is only needed when saving a game is added.
     * @throws XMLStreamException if there are any problems writing
     *      to the stream.
     */
    protected void toXMLImpl(XMLStreamWriter out) throws XMLStreamException {
        // Start element:
        out.writeStartElement(getXMLElementTagName());

        out.writeAttribute("ID", getId());
        out.writeAttribute("amount", Integer.toString(amountInMarket));
        out.writeAttribute("arrears", Integer.toString(arrears));
        out.writeAttribute("sales", Integer.toString(sales));
        out.writeAttribute("incomeBeforeTaxes", Integer.toString(incomeBeforeTaxes));
        out.writeAttribute("incomeAfterTaxes", Integer.toString(incomeAfterTaxes));

        out.writeEndElement();
    }

    /**
     * Initialize this object from an XML-representation of this object.
     * @param in The input stream with the XML.
     */
    protected void readFromXMLImpl(XMLStreamReader in) throws XMLStreamException {
        setId(in.getAttributeValue(null, "ID"));
        amountInMarket = Integer.parseInt(in.getAttributeValue(null, "amount"));
        arrears = Integer.parseInt(in.getAttributeValue(null, "arrears"));
        sales = Integer.parseInt(in.getAttributeValue(null, "sales"));
        incomeBeforeTaxes = Integer.parseInt(in.getAttributeValue(null, "incomeBeforeTaxes"));
        incomeAfterTaxes = Integer.parseInt(in.getAttributeValue(null, "incomeAfterTaxes"));
    
        in.nextTag();
    }

    /**
     * Returns the tag name of the root element representing this object.
     *
     * @return the tag name.
     */
    public static String getXMLElementTagName() {
        return "marketData";
    }

} 
