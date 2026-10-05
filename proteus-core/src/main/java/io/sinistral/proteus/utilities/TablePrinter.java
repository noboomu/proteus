/**
 *
 */
package io.sinistral.proteus.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Plain-text table formatter used for startup endpoint listings.
 *
 * @author jbauer
 */
public class TablePrinter
{
    /** The tablepadding. */
    private final int TABLEPADDING = 4;
    /** The headers. */
    private List<String> headers;
    /** The table. */
    private List<List<String>> table;
    /** The max length. */
    private List<Integer> maxLength;

    /** Creates a printer from headers and rows.
     *
     * @param headersIn the header labels
     * @param content the table rows */
    public TablePrinter(List<String> headersIn, List<List<String>> content)
    {
        this.headers = headersIn;
        this.maxLength = new ArrayList<Integer>();

        for (int i = 0; i < headers.size(); i++) {
            maxLength.add(headers.get(i).length());
        }

        this.table = content;

        updateMaxLengths();
    }

    /**
     * Returns a human-readable rendering.
     *
     * @return the string form
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder rowSeparatorBuilder = new StringBuilder();
        String padder = "";
        String rowSeperator = "";

        for (int i = 0; i < 4; i++) {
            padder += " ";
        }

        for (int i = 0; i < maxLength.size(); i++) {
            for (int j = 0; j < maxLength.get(i) + (TABLEPADDING * 2); j++) {
                rowSeparatorBuilder.append("-");
            }
        }

        rowSeperator = rowSeparatorBuilder.toString();

        sb.append("\n");

        for (int i = 0; i < headers.size(); i++) {
            sb.append(padder);
            sb.append(headers.get(i));

            for (int k = 0; k < (maxLength.get(i) - headers.get(i).length()); k++) {
                sb.append(" ");
            }

            sb.append(padder);
        }

        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");

        for (int i = 0; i < table.size(); i++) {
            List<String> tempRow = table.get(i);

            for (int j = 0; j < tempRow.size(); j++) {
                sb.append(padder);
                sb.append(tempRow.get(j));

                for (int k = 0; k < (maxLength.get(j) - tempRow.get(j).length()); k++) {
                    sb.append(" ");
                }

                sb.append(padder);
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Sets the update field and returns this instance.
     *
     * @param row the update field
     * @param col the update field
     * @param input the update field
     */
    public void updateField(int row, int col, String input)
    {
        table.get(row).set(col, input);
        updateMaxColumnLength(col);
    }

    private void updateMaxColumnLength(int col)
    {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).get(col).length() > maxLength.get(col)) {
                maxLength.set(col, table.get(i).get(col).length());
            }
        }
    }

    private void updateMaxLengths()
    {
        for (int i = 0; i < table.size(); i++) {
            List<String> temp = table.get(i);

            for (int j = 0; j < temp.size(); j++) {
                if (temp.get(j).length() > maxLength.get(j)) {
                    maxLength.set(j, temp.get(j).length());
                }
            }
        }
    }
}



