package org.drools.examples.java.petstore;

/*
 $Id: PetStoreUI.java,v 1.2 2004-06-26 15:45:16 mproctor Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
 statements and notices.  Redistributions must also contain a
 copy of this document.

 2. Redistributions in binary form must reproduce the
 above copyright notice, this list of conditions and the
 following disclaimer in the documentation and/or other
 materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
 products derived from this Software without prior written
 permission of The Werken Company.  For written permission,
 please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
 nor may "drools" appear in their names without prior written
 permission of The Werken Company. "drools" is a trademark of
 The Werken Company.

 5. Due credit should be given to The Werken Company.
 (http://werken.com/)

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * @author mproctor
 * 
 * This swing UI is used to create a simple shopping cart to allow a user to add
 * and remove items from a shopping cart before doign a checkout upon doing a
 * checkout a callback is used to allow drools interaction with the shopping
 * cart ui.
 */
public class PetStoreUI extends JPanel
{
	private JTextArea output;

	private TableModel tableModel;

	private CheckoutCallback callback;

	/**
	 * Build UI using specified items and using the given callback to pass the
	 * items and jframe reference to the drools application
	 * 
	 * @param listData
	 * @param callback
	 */
	public PetStoreUI( Vector items, CheckoutCallback callback )
	{
		super( new BorderLayout( ) );
		this.callback = callback;

		//Create main vertical split panel
		JSplitPane splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		add( splitPane, BorderLayout.CENTER );

		//create top half of split panel and add to parent
		JPanel topHalf = new JPanel( );
		topHalf.setLayout( new BoxLayout( topHalf, BoxLayout.LINE_AXIS ) );
		topHalf.setBorder( BorderFactory.createEmptyBorder( 5, 5, 0, 5 ) );
		topHalf.setMinimumSize( new Dimension( 400, 50 ) );
		topHalf.setPreferredSize( new Dimension( 450, 250 ) );
		splitPane.add( topHalf );

		//create bottom top half of split panel and add to parent
		JPanel bottomHalf = new JPanel( new BorderLayout( ) );
		bottomHalf.setMinimumSize( new Dimension( 400, 50 ) );
		bottomHalf.setPreferredSize( new Dimension( 450, 300 ) );
		splitPane.add( bottomHalf );

		//Container that list container that shows available store items
		JPanel listContainer = new JPanel( new GridLayout( 1, 1 ) );
		listContainer.setBorder( BorderFactory.createTitledBorder( "List" ) );
		topHalf.add( listContainer );

		//Create JList for items, add to scroll pane and then add to parent
		// container
		JList list = new JList( items );
		ListSelectionModel listSelectionModel = list.getSelectionModel( );
		listSelectionModel
				.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		//handler adds item to shopping cart
		list.addMouseListener( new ListSelectionHandler( ) );
		JScrollPane listPane = new JScrollPane( list );
		listContainer.add( listPane );

		JPanel tableContainer = new JPanel( new GridLayout( 1, 1 ) );
		tableContainer.setBorder( BorderFactory.createTitledBorder( "Table" ) );
		topHalf.add( tableContainer );

		//Container that displays table showing items in cart
		tableModel = new TableModel( );
		JTable table = new JTable( tableModel );
		//handler removes item to shopping cart
		table.addMouseListener( new TableSelectionHandler( ) );
		ListSelectionModel tableSelectionModel = table.getSelectionModel( );
		tableSelectionModel
				.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		TableColumnModel tableColumnModel = table.getColumnModel( );
		//notice we have a custom renderer for each column as both columns
		// point to the same underlying object
		tableColumnModel.getColumn( 0 ).setCellRenderer( new NameRenderer( ) );
		tableColumnModel.getColumn( 1 ).setCellRenderer( new PriceRenderer( ) );
		tableColumnModel.getColumn( 1 ).setMaxWidth( 50 );

		JScrollPane tablePane = new JScrollPane( table );
		tablePane.setPreferredSize( new Dimension( 150, 100 ) );
		tableContainer.add( tablePane );

		//Create panel for checkout button and add to bottomHalf parent
		JPanel checkoutPane = new JPanel( );
		JButton button = new JButton( "Checkout" );
		button.setVerticalTextPosition( AbstractButton.CENTER );
		button.setHorizontalTextPosition( AbstractButton.LEADING );
		//attach handler to assert items into working memory
		button.addMouseListener( new CheckoutButtonHandler( ) );
		button.setActionCommand( "checkout" );
		checkoutPane.add( button );
		bottomHalf.add( checkoutPane, BorderLayout.PAGE_START );

		button = new JButton( "Reset" );
		button.setVerticalTextPosition( AbstractButton.CENTER );
		button.setHorizontalTextPosition( AbstractButton.TRAILING );
		//attach handler to assert items into working memory
		button.addMouseListener( new ResetButtonHandler( ) );
		button.setActionCommand( "reset" );
		checkoutPane.add( button );
		bottomHalf.add( checkoutPane, BorderLayout.PAGE_START );

		//Create output area, imbed in scroll area an add to bottomHalf parent
		//Scope is at instance level so it can be easily referenced from other
		// methods
		output = new JTextArea( 1, 10 );
		output.setEditable( false );
		JScrollPane outputPane = new JScrollPane( output,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		bottomHalf.add( outputPane, BorderLayout.CENTER );
	}

	/**
	 * Create and show the GUI
	 *  
	 */
	public void createAndShowGUI()
	{
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated( true );

		//Create and set up the window.
		JFrame frame = new JFrame( "Pet Store Demo" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		setOpaque( true );
		frame.setContentPane( this );

		//Display the window.
		frame.pack( );
		frame.setVisible( true );
	}

	/**
	 * Adds the selected item to the table
	 */
	private class ListSelectionHandler extends MouseAdapter
	{
		public void mouseReleased( MouseEvent e )
		{
			JList jlist = (JList) e.getSource( );
			tableModel.addItem( (CartItem) jlist.getSelectedValue( ) );
		}
	}

	/**
	 * Removes the selected item from the table
	 */
	private class TableSelectionHandler extends MouseAdapter
	{
		public void mouseReleased( MouseEvent e )
		{
			JTable jtable = (JTable) e.getSource( );
			TableModel tableModel = (TableModel) jtable.getModel( );
			tableModel.removeItem( jtable.getSelectedRow( ) );
		}
	}

	/**
	 * Calls the referenced callback, passing a the jrame and selected items.
	 *  
	 */
	private class CheckoutButtonHandler extends MouseAdapter
	{
		public void mouseReleased( MouseEvent e )
		{
			JButton button = (JButton) e.getComponent( );
			try
			{
				output.append( callback.checkout( (JFrame) button
						.getTopLevelAncestor( ), tableModel.getItems( ) ) );
			}
			catch ( org.drools.FactException fe )
			{
				fe.printStackTrace( );
			}
		}
	}

	/**
	 * Resets the shopping cart, allowing the user to begin again.
	 *  
	 */
	private class ResetButtonHandler extends MouseAdapter
	{
		public void mouseReleased( MouseEvent e )
		{
			JButton button = (JButton) e.getComponent( );
			output.setText( null );
			tableModel.clear( );
			System.out.println( "------ Reset ------" );
		}
	}

	/**
	 * Used to render the name column in the table
	 */
	private class NameRenderer extends DefaultTableCellRenderer
	{
		public NameRenderer()
		{
			super( );
		}

		public void setValue( Object object )
		{
			CartItem item = (CartItem) object;
			setText( item.getName( ) );
		}
	}

	/**
	 * Used to render the price column in the table
	 */
	private class PriceRenderer extends DefaultTableCellRenderer
	{
		public PriceRenderer()
		{
			super( );
		}

		public void setValue( Object object )
		{
			CartItem item = (CartItem) object;
			setText( Double.toString( item.getCost( ) ) );
		}
	}

	/**
	 * This is the table model used to represent the users shopping cart While
	 * we have two colums, both columns point to the same object. We user a
	 * different renderer to display the different information abou the object -
	 * name and price.
	 */
	private class TableModel extends AbstractTableModel
	{
		private String[] columnNames = {"Name", "Price"};
		private ArrayList items;

		public TableModel()
		{
			super( );
			items = new ArrayList( );
		}

		public int getColumnCount()
		{
			return columnNames.length;
		}

		public int getRowCount()
		{
			return items.size( );
		}

		public String getColumnName( int col )
		{
			return columnNames[col];
		}

		public Object getValueAt( int row, int col )
		{
			return items.get( row );
		}

		public Class getColumnClass( int c )
		{
			return CartItem.class;
		}

		public void addItem( CartItem item )
		{
			items.add( item );
			fireTableRowsInserted( items.size( ), items.size( ) );
		}

		public void removeItem( int row )
		{
			items.remove( row );
			fireTableRowsDeleted( row, row );
		}

		public List getItems()
		{
			return items;
		}

		public void clear()
		{
			int lastRow = items.size( );
			items.clear( );
			fireTableRowsDeleted( 0, lastRow );
		}
	}

}

