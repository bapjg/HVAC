package eReg_Forms;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class OefeningenTableModel extends AbstractTableModel {

    ArrayList<Oefening> oefeningen;

    public OefeningenTableModel(){
        //oefeningen = DAOFactory.getFactory(0).getIDAOOefening().load();
    }    

    @Override
    public int getRowCount() {
        return oefeningen.size();
    }

    @Override
    public int getColumnCount() {
        return 22;// You'll need to fill this out to meet your requirements
    }

    @Override
    public String getColumnName(int column) {
        return "heading";// You'll need to fill this out to meet your requirements
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return 33;// You'll need to fill this out to meet your requirements
    }

}
