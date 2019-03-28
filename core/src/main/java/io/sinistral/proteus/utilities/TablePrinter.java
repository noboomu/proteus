/**
 *
 */
package io.sinistral.proteus.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jbauer
 */
public class TablePrinter
{
    private final int TABLEPADDING = 4;
    private List<String> headers;
    private List<List<String>> table;
    private List<Integer> maxLength;

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



