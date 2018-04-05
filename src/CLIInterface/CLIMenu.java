package CLIInterface;


import java.util.Scanner;
import java.util.Vector;

/*
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License, version 3,
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 *
 * @author Hossein Ebrahimi
 */
public class CLIMenu {
    private String m_title;
    private Vector<String> m_menu = new Vector<String>(1);
    
    public CLIMenu(String menutitle){
        m_title = menutitle;   
    }
    public CLIMenu(){
        m_title = "";
    }   
    //-------------------------------------
    /**
     * Processes the input by first converting it to
     * lower case, then by eliminating stop words, and
     * finally by performing Porter stemming on it.
     *
     * @param menutitle A string that will be used as menu title
     * @return none
     */    
    public void add(String menuitem){
        m_menu.addElement(menuitem);        
    }
    /**
     * Processes the input by first converting it to
     * lower case, then by eliminating stop words, and
     * finally by performing Porter stemming on it.
     *
     * @param non
     * @return selected item otherwise -1
     */     
    public int run(){
        System.out.println("\n" + m_title);
        if (!m_menu.isEmpty()){
            for (int i = 0; i < m_menu.size(); i++)
                System.out.printf("%d - %s\n",i+1,m_menu.get(i));

            Scanner input = new Scanner(System.in);

            System.out.println("Please enter your choice : ");
            int i = input.nextInt(); // getting an integer
            if ( i > 0 && i <= m_menu.size())
                return i;
        }
        return -1;
    }    
}
