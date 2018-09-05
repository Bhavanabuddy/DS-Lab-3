/* NAME: BHAVANA VADLAMUDI  STD ID: 1001572758*/
package com.mclient;

import com.mclient.util.ChatUtil;
import com.mclient.*;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.plaf.metal.*;
import java.beans.*;
import javax.swing.border.EtchedBorder;
import java.awt.Toolkit.*;
import javax.swing.text.*;
import java.sql.*;


public class Client extends JFrame
{
        public String x="";
        private static String query="";
private static String question;
private static int time=5;
private static int cnt=0;
private static boolean invalid=false;
	private static long start,end;
	private static boolean alive;
	private static long ackstart,ackend;
        private static ArrayList<String> answers;
        private static ArrayList<String> responses;
        private static int count;		
		
    private static ArrayList<String> fanswers;
    private static ArrayList<String> fresponses;
		
    private static String myrole="xxx";
	private boolean isposted=false;
	private boolean isvoted=false;
 

private int downloadcount=0;
		
        private JTextPane output = null;
        private JTextField input = null;
        private JMenuBar menuBar = null;
        private JList list = null;

        private JPanel emoticons = null;
        private JButton smiley = null;
        private JButton sad = null;
        private JButton send = null;
        public static JButton commit=null;
        public static JButton abort=null;
		public static JButton ack=null;
		

        private JScrollPane listScrollPane = null;
        

 
        private ObjectOutputStream out = null;
        private ObjectInputStream in = null;
        private Socket client = null;

        private ChatUtil chatUtil = null;

        private File file = null;
        private FileInputStream fin = null;
        private FileOutputStream fout = null;
        private String name = null;
        private String serverName = null;
        private String fileName = null;
        private String privateBuddy = null;
        private String gamePall = null;
        private Vector vectorList = null;
        private Vector v = null;
        private Container container = null;
        private JPanel ePanel = null;

        private ImageIcon killerIcon = null;
        private ImageIcon smileIcon = null;
        private ImageIcon sadIcon = null;
        private ImageIcon sendIcon = null;
        private boolean isEmoticonDisabled = false;
		private ImageIcon downIcon = null;
		

        private String nameBlock = null;
        private String nameBuzz = null;
        private String strLogin, strPassword = null;
		public String myname=null;
		
        private Login login = null;
		private NewUser n = null;
	 
        

        private int countChat = 0;
        private int countChat2 = 0;
       

		String udata="";
		int newcreate=0;

	TimerTask task;
        public Client(String x1){
            super("HTTP Messenger Service ");
            x=x1;

            chatUtil = new ChatUtil();
            container = getContentPane();
            output = new JTextPane();
            output.setEditable(false);
            JScrollPane pane = new JScrollPane(output);

            addWindowListener( new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    try{
                        if (out!=null){
                            out.writeObject(name + ": has exited the Chat. Bye-bye!");
                            out.writeObject("DISCONNECTED#"+name);
                        }
                    }
                    catch (IOException ioe){
                        System.out.println("Client:> exception at windowListener\n" + ioe.getLocalizedMessage());
                    }
                    System.exit(0);
                }
            });


            input = new JTextField();
         //   input.setBorder(null);
            JScrollPane inputScroll = new JScrollPane(input);
            inputScroll.setPreferredSize(new Dimension(0, 50));
            input.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent ke){
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                        sendText();
                    }
                }
            });

            input.setCaretPosition(0);
            killerIcon = new ImageIcon("killer.gif");
            sadIcon = new ImageIcon("sad.gif");
            smileIcon = new ImageIcon("smiley.gif");
            sendIcon = new ImageIcon("send.gif");
			downIcon= new ImageIcon("user.gif");
			
            ePanel = new JPanel();
            ePanel.setLayout(new FlowLayout());
			
            //ePanel.setPreferredSize(new Dimension(64, 60));//   2*ICONHEIGHT
//			ePanel.setPreferredSize(new Dimension(32, 90));//   2*ICONHEIGHT
            ePanel.setPreferredSize(new Dimension(90, 180));//   2*ICONHEIGHT

            emoticons = new JPanel();
            emoticons.setPreferredSize(new Dimension(32, this.getHeight() - 80));
            emoticons.setLayout(new BorderLayout());

			commit=new JButton("Comit");
	commit.setPreferredSize(new Dimension(80,32));

	abort=new JButton("Abort ");
	abort.setPreferredSize(new Dimension(80,32));
	
    ack=new JButton("ACK");
	ack.setPreferredSize(new Dimension(80,32));
	   
			
            if (smileIcon.getIconHeight() > 0){
                smiley = new JButton(smileIcon);
                smiley.setPreferredSize(new Dimension(45,22));//22
            }
            else{
                smiley = new JButton(":-)");
                smiley.setPreferredSize(new Dimension(45,22));
            }
                smiley.setBorderPainted(false);

            if (sendIcon.getIconHeight() > 0){
                send = new JButton(sendIcon);
                send.setPreferredSize(new Dimension(32,32));
            }
            else{
                send = new JButton(">>");
                send.setPreferredSize(new Dimension(50, 32));
                //send.setBorderPainted(false);
            }

            if (sadIcon.getIconHeight() > 0){
                sad = new JButton(sadIcon);
                sad.setPreferredSize(new Dimension(45,22));//22
            }
            else{
                sad = new JButton(":-(");
                sad.setPreferredSize(new Dimension(45,22));
            }
            sad.setBorderPainted(false);


			vectorList = new Vector();
            vectorList.setSize(1);
            list = new JList(vectorList);
            list.setFixedCellWidth(70);
            list.setFixedCellHeight(20);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listScrollPane = new JScrollPane(list);
            listScrollPane.setAutoscrolls(true);

			
            sad.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    try{
                        out.writeObject("SAD#"+name);
                  }
                  catch(IOException ioe){
                      System.out.println("Client:> exception at sad button mouse listener\n" + ioe.getLocalizedMessage());
                  }
                }
            });
				 
			
            send.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    sendText();
                }
            });

            commit.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    sendCommit();
                }
            });
            abort.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    sendAbort();
                }
            });

			ack.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    sendAck();
                }
            });

            smiley.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    try{
                        out.writeObject("SMILE#"+name);
                    }
                    catch(IOException ioe){
                        System.out.println("Client:> exception at smiley button mouse listener\n" + ioe.getLocalizedMessage());
                    }
                }
            });

            ePanel.add(smiley);
            ePanel.add(sad);
            ePanel.add(commit);
            ePanel.add(abort);
			ePanel.add(ack);

         emoticons.add(ePanel, BorderLayout.NORTH);

            try{
                serverName = getServerName();
                if (serverName == null)
                    serverName =  InetAddress.getLocalHost().toString();
            }
            catch(IOException ioe){
                serverName = "localhost";
            }
 
            list.addMouseListener(createMouseListener());
            container.add(listScrollPane, BorderLayout.WEST);
            container.add(pane, BorderLayout.CENTER);
            container.add(inputScroll, BorderLayout.SOUTH);
            container.add(createMenuBar(), BorderLayout.NORTH);
            container.add(emoticons, BorderLayout.EAST);

            setSize(300,400);
            setVisible(true);

        }// end Client

private void updateControls()
{
String currentname="";
String commituser="";
boolean flag=false;
 
if(list.getModel().getSize()>0)
{
 for(int i=0;i<list.getModel().getSize();i++)
{
currentname=list.getModel().getElementAt(i).toString();
if(name.equals(currentname.substring(0,currentname.indexOf("("))))
{
    int nx=currentname.indexOf("(")+1;
    char next=currentname.charAt(nx);
    if(next=='C')
	{
		flag=true;
		break;
	}
    else
	{
		flag=false;
		break;
	}
}//end of if 
}//end of for

	if(flag)
		{
			
			ackend=System.currentTimeMillis();
			commit.setEnabled(false);
			myrole="CO";
      commit.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                }
            });
      
	  ack.setEnabled(false);
			myrole="CO";
      ack.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                }
            });
      
			abort.setEnabled(false);
			myrole="CO";
      abort.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                }
            });
			
			try
			{
			String finaltext=" \nPrevious LogFile Result For This Client \n ******************************\n";
					FileInputStream fis=new FileInputStream(name+"(C).txt");
					BufferedReader br=new BufferedReader(new InputStreamReader(fis));
					
					String line=br.readLine();
					while(line!=null)
					{
						 finaltext+=line + "\r\n";
						 line=br.readLine();
					}
					finaltext+="\r\n ***********************************\n\n";
					br.close();
					fis.close();

					appendDisplay(output,finaltext + "\n");	
			}
			catch(Exception ex)
			{
				appendDisplay(output,"\nNo previous Log File Found For This User \n");	
			}
 
		}
	else
		{
			myrole="PA";
			commit.setEnabled(true);
			abort.setEnabled(true);
			ack.setEnabled(false);
			ack.setText("Ok");
			//show the contents of the log file back to the screen which is non-voliatle memory 
		try
		{
			 
					String finaltext=" Previous LogFile Result For This Client \n ******************************\n";
					FileInputStream fis=new FileInputStream(name+"(P).txt");
					BufferedReader br=new BufferedReader(new InputStreamReader(fis));
					
					String line=br.readLine();
					while(line!=null)
					{
						 finaltext+=line + "\r\n";
						 line=br.readLine();
					}
					finaltext+="\r\n ***********************************\n\n";
					br.close();
					fis.close();

					appendDisplay(output,finaltext + "\n");	
		}
		catch(Exception ex)
		{
			appendDisplay(output,"\nNo previous Log File Found For This User \n");	
			//JOptionPane.showMessageDialog(this,"Error Occurred In Log File Retrieval");
		}
		}
}
}
public void sendCommit()
{
try
{
int idx=0;
String currentname="";
String commituser="";
 for(int i=0;i<list.getModel().getSize();i++)
{
currentname=list.getModel().getElementAt(i).toString();
if(name.equals(currentname.substring(0,currentname.indexOf("("))))
{
commituser=name;
idx=i;
break;
}
}
list.setSelectedIndex(idx);
out.writeObject(commituser + " posted answer   with  Pre-Commit");
out.writeObject("ANSWER#"+commituser + "#COMMIT");
}
catch(Exception ex)
{
                System.out.println("Client:> exception at CommitKeyListener\n" + ex.getLocalizedMessage());
}
}
public void sendAck()
{
	try
	{
	   if(ack.getText().startsWith("ACK")) //for coordinator when he acknowledes again
	   {
		out.writeObject(name + " Is Asking Acknoledgment From Clients ");
		out.writeObject("ACK#"+name+"#OK");
		ackstart=System.currentTimeMillis();
	   }
	else  //if the user responds to the acknowledement 
	{
			out.writeObject(name + " Said \"Yes\" To Coordinator Acknowledgment ");
			out.writeObject("CONFIRM#"+name+"#YES");
	}	
	}
	catch(Exception ex)
	{
		System.out.println("Client:> exception at AbortKeyListener\n" + ex.getLocalizedMessage());
	}
}
public void sendAbort()
{
try
{
int idx=0;
String currentname="";
String commituser="";
 for(int i=0;i<list.getModel().getSize();i++)
{
currentname=list.getModel().getElementAt(i).toString();
  if(name.equals(currentname.substring(0,currentname.indexOf("("))))
{
commituser=name;
		idx=i;
break;
}
}
list.setSelectedIndex(idx);
out.writeObject(commituser + " posted answer   with  Abort");
out.writeObject("ANSWER#"+commituser+"#ABORT");
}
catch(Exception ex)
{
                System.out.println("Client:> exception at AbortKeyListener\n" + ex.getLocalizedMessage());
				}
}


        public void sendText(){
            String text = input.getText();
             try{
                if (out!=null)
				{
                    if (text.equals(":)") || text.equals(":-)"))
                        out.writeObject("SMILE#"+name);
                    else if (text.equals(":(") || text.equals(":-("))
                        out.writeObject("SAD#"+name);
                    else if (text.equals(">:)"))
                        out.writeObject("KILLER#"+name);
                    else
		{
//write logic of time here
out.writeObject(name + ":" + "%" + text + "%" + System.currentTimeMillis() + "%Method:Post Content-Type:Text/Plain Content-Length" + text.length() + " " + new java.util.Date().toString());
					}
                }
            }
            catch (IOException ioe){
                System.out.println("Client:> exception at KeyListener\n" + ioe.getLocalizedMessage());
            }
            input.setText("");
        }
        public MouseListener createMouseListener(){
            MouseListener ml = new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    int button = me.getModifiers();
                    if (button == MouseEvent.BUTTON3_MASK){
                    int index = list.locationToIndex(me.getPoint());
                        if (index != -1){
                            list.setSelectedIndex(index);
                            showPopup(me);
                        }
                    }
                }
                public void mouseClicked(MouseEvent me){
                    int button = me.getModifiers();
                    if (me.getClickCount() == 2) {
                        int index = list.locationToIndex(me.getPoint());
                    }
                    if (me.MOUSE_RELEASED == me.BUTTON2_MASK){
                        int index = list.locationToIndex(me.getPoint());
                    }
                }
            };
           return ml;
        }


        public void showPopup(MouseEvent me){

        }

        private int getPosition(String window){
            int result = 0;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int)screen.getWidth();
            int screenHeight = (int)screen.getHeight();
            int width = this.getWidth();
            int height = this.getHeight();
            Point p = new Point();
            p = this.getLocation();
            if (window.equals("chat")){
                if (p.x < width){
                    result = 1;
                    return result; // SPACE ON RIGHT
                }
            }
            /*else if(window.equals("game")){
                if (p.y > height){
                    result = 1;
                    return result; // SPACE ON TOP
                }
            }*/
            return result;
        }


        private JMenuBar createMenuBar(){
            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createMenu("File"));
        return menuBar;
        }

        private JMenu createMenu(String title){
            JMenu menu = new JMenu();
            menu.setText(title);
            if(title.equals("File"))
{
                menu.setMnemonic(KeyEvent.VK_F);
                menu.add(createItem("Save"));
                menu.add(createItem("Clear"));
                 menu.add(createItem("Post"));
	menu.add(createItem("Vote"));
                 menu.add(createItem("Exit"));
            }
        return menu;
        }


        private JMenuItem createItem(String title){
            JMenuItem menuItem = new JMenuItem(title);
            if (title.equals("Save"))
                menuItem.setMnemonic(KeyEvent.VK_S);
            if (title.equals("Clear"))
                menuItem.setMnemonic(KeyEvent.VK_C);
			if (title.equals("Exit"))
                menuItem.setMnemonic(KeyEvent.VK_X);
			else if (title.equals("Vote"))
                menuItem.setMnemonic(KeyEvent.VK_V);
			else if (title.equals("Post"))
                menuItem.setMnemonic(KeyEvent.VK_P);
            menuItem.addActionListener(createActionListener(title));
            return menuItem;
        }

        private JCheckBoxMenuItem createCheckItem(String title){
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(title);
            if (title.equals("Disable emoticons"))
                jCheckBoxMenuItem.setMnemonic(KeyEvent.VK_D);
            jCheckBoxMenuItem.addActionListener(createActionListener(title));
            return jCheckBoxMenuItem;
        }

        private ActionListener createActionListener(String s){
            ActionListener a = null;
            if(s.equals("Clear")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        output.setText("");
                    }
                };
            }
            else if(s.equals("Save")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        chatUtil.saveLog(output);
                    }
                };
            }
 else if(s.equals("Vote"))
{
	//first check whether a string is posted or not if so then ask voting for participants
	    a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        try
           {
	if(!isposted)
	{
		JOptionPane.showMessageDialog(new JFrame(),"No Question Is Yet Posted So U cannot Ask Votes ");
		return;
	}
	    if(myrole.equals("CO"))
	{
		  String defanswer="COMMIT (or) ABORT";
	       String question=JOptionPane.showInputDialog("COMMIT (OR) ABORT ?  ","COMMIT (or) ABORT ");
                            if (out != null)
      	{
			end=System.currentTimeMillis();
			//JOptionPane.showMessageDialog(new JFrame(),((end-start)/1000));
			if((end-start)/1000>25)
			{
				JOptionPane.showMessageDialog(new JFrame(),"TimeOut is Over ");
				out.writeObject("VOTE#" + name + "#GABORT");
				return;
			}
			else
        out.writeObject(name + " [ Vote Now : " + defanswer + "  ] ");
		start=System.currentTimeMillis();
              /* task=new TimerTask()
	{
 	public void run() 
	{
        // task to run goes here
		cnt++;	
		if(cnt>time)
		{
	try
	{
		out.writeObject("ANSWER#GLOBAL"+"#ABORT");
		return;
	}
	catch(Exception ex)
	{

	}
		}        
	      }
	};*/
		isposted=false;
		isvoted=true;
                 }
                }
  else
	JOptionPane.showMessageDialog(new JFrame(),"U cannot post Voting Option As U are Participant ");
                                        }
                        catch (IOException ioe){
                            System.out.println("Client:> exception at exitActionListener\n" + ioe.getLocalizedMessage());
                        }
                    }
                };
	
 }
   else if(s.equals("Post")) 
            {
	    a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        try
           {
	    if(myrole.equals("CO"))
	{
	       question=JOptionPane.showInputDialog("Enter Arbitary String Now ?  ");
                            if (out != null)
      	{
	          isposted=true;
			  start=System.currentTimeMillis();
                            out.writeObject(name + " [ Q : " + question+ " ? ] ");
                            out.writeObject("QUESTION#"+name+"#"+question+"#Method:Post Content-Type:Text/Plain Content-Length" + question.length() + " " + new java.util.Date().toString());
                 }
                }
  else
	JOptionPane.showMessageDialog(new JFrame(),"U cannot post Arbitary String As U are Participant ");
                                        }
                        catch (IOException ioe){
                            System.out.println("Client:> exception at exitActionListener\n" + ioe.getLocalizedMessage());
                        }
                    }
                };
			}
            else if (s.equals("Exit")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        try{
                            if (out != null){
                            out.writeObject(name + " has exited the Chat. Bye-bye!");
                            out.writeObject("DISCONNECTED#"+name);
                            }
                        }
                        catch (IOException ioe){
                            System.out.println("Client:> exception at exitActionListener\n" + ioe.getLocalizedMessage());
                        }
                        System.exit(0);
                    }
                };
            }
            
            else if (s.equals("Disable emoticons")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        disableFaces();
                    }
                };
            }
            else if (s.equals("About")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        about();
                    }
                };
            }
           
            return a;
        }
        public void about()
{
            JOptionPane.showMessageDialog(this, "Local Messenger System \n           by\n", "About", JOptionPane.INFORMATION_MESSAGE);
        }

        public Socket getClient(){
            return client;
        }

        public ObjectOutputStream getOut(){
            return out;
        }

        public ObjectInputStream getIn(){
            return in;
        }

      
        public void showListMessage(){
            JOptionPane.showMessageDialog(this, "You must select a person from the left list", "None selected", JOptionPane.INFORMATION_MESSAGE);
        }

        public void disableFaces(){
            if (isEmoticonDisabled){
                isEmoticonDisabled = false;
                smiley.setText(null);
                smiley.setIcon(smileIcon);
                sad.setText(null);
                sad.setIcon(sadIcon);
            }
            else{
                isEmoticonDisabled = true;
                smiley.setIcon(null);
                smiley.setText(":-)");
                sad.setIcon(null);
                sad.setText(":-(");
            }
            smiley.invalidate();
            sad.invalidate();
        }

        public void setLogin(String login){
            strLogin = login;
        }

        public void setPassword(String pass){
            strPassword = pass;
        }


        private void doLogin(){
			//JOptionPane.showMessageDialog(this,"Activated this phase " + login.getNewUser());
            do{
				//JOptionPane.showMessageDialog(this,"USER :" + login);
				 
                if (login != null)
                login.setVisible(true);
                else
				{
					
                    login = new Login(new JDialog(), "HTTP CLIENT LOGIN", true);
					//JOptionPane.showMessageDialog(this,"user " + login.getNewUser());
                    login.setSize(250,150);
                    login.setLocationRelativeTo(this);
                    login.setVisible(true);
                    login.setResizable(false);
                    login.toFront();
					strLogin = login.getLogin();
                strPassword = login.getPassword();
                name = strLogin;
				myname=strLogin;
                this.setTitle(name);
				//JOptionPane.showMessageDialog(this,strLogin + "--" + strPassword);
				//break;
				   }
				   
				 //JOptionPane.showMessageDialog(this,"user " + login.getNewUser());
				if(login.getNewUser())
				{
					//JOptionPane.showMessageDialog(this,"New User in activation");
					n = new NewUser(new JDialog(), "NEW HTTP CLIENT",true);
					n.setSize(200,400);
					n.setLocationRelativeTo(this);
                   // n.setVisible(true);
                    n.setResizable(true);
                    n.toFront();
					udata=n.retData();
					 
					name = n.getName();
					myname=n.getName();
					strLogin = n.getName();
                strPassword = n.getPwd();
					newcreate=1;
					
					this.setTitle(name);
					//runClient();
					break;
				}
    			
                this.setTitle(name);
				 
            }while(strLogin == null || strPassword == null || strLogin.equals("") || strPassword.equals(""));
			            runClient();
        }

		/*
 *
 *  RunClient()
 *
 */

        public void runClient(){
            String line = new String(" ");
            Object objReceived = null;
            try{
                  client = new Socket(x,5000);
                 out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                StringTokenizer strT = null;
                String nt = null;
				if(newcreate==1)
				{
					try{
					 	if(out!=null)
						{
					 				out.writeObject("NEWUSER#"+udata);
						}
					}catch (IOException ioe){
			                System.out.println(ioe.getLocalizedMessage());
					}
				}
				try{
                    if (out!=null)
					{
                        input.setText("");
	
                        out.writeObject("NAMEOK#" + strLogin + "#" + strPassword);
                    }
                }
                catch (IOException ioe){
                    System.out.println("Exception caught " + ioe);
                }
				
				
//  Main loop DO
                do{
                    objReceived = in.readObject();
                    int option = 0;
					FileOutputStream fos;
			PrintWriter pw;
                    //  flash the window to notify user something was received
                    if (this.getState() == JFrame.ICONIFIED || !this.isFocused())
                        this.setVisible(true);
		// STRING
                    if(objReceived instanceof String)
					{
                        line = (String)objReceived;
  
                        strT = new StringTokenizer(line, "#");
                        if (strT.countTokens() > 1)
            {
                            nt = strT.nextToken();
                            if (nt.equals("NAMEREJECTED")){
                                String n = strT.nextToken();
                                JOptionPane.showMessageDialog(this, n + " is not a valid user.", "Login error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            else if (nt.equals("DBERROR")){
                                String n = strT.nextToken();
                                JOptionPane.showMessageDialog(this, "Database error.", n+": login error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            else if (nt.equals("WRONGPASSWORD")){
                                String n = strT.nextToken();
                                JOptionPane.showMessageDialog(this, "The password does not match.", n+": login error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
							else
								if(nt.equals("User Registered"))
								{
								JOptionPane.showMessageDialog(this,"Successfully Registered ");
								System.exit(1);
								}
							else
								if(nt.equals("User Not Registered"))
								{
								JOptionPane.showMessageDialog(this,"Not Able To Register With Application ");
								System.exit(1);
								}
                            else if (nt.equals("NUKE")){
                                JOptionPane.showMessageDialog(this, "The server chose to shut you down.", "Dave, Dave...", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            else if (nt.equals("SERVER")){
                                appendDisplay(output, "Server: "+strT.nextToken()+"\n");
                            }
		
		else if(nt.equals("TIMEOUT"))
		{
String fromwhom=strT.nextToken();
 boolean flag=false;

  if(output.getText().indexOf("Global Abort Done Based On TimeFactor")<0)
	appendDisplay(output,"\n<< Global Abort Done Based on TimeFactor>>");
  if(output.getText().indexOf("TIMEOUT ABORT DONE")<0)
	  appendDisplay(output,"\nTIMEOUT ABORT DONE\n");   
  
					fos=new FileOutputStream(fromwhom + "(C)"+".txt",true);
					pw=new PrintWriter(fos);		
					pw.println(query + "   " + fromwhom + "(C)" + "   ABORT BY TIMEOUT FACTOR ");
					pw.close();
					fos.close();
		}
else if(nt.equals("GLOBALABORTDONE"))
{
 String fromwhom=strT.nextToken();
 boolean flag=false;

  if(output.getText().indexOf("Global Abort Done")<0)
	appendDisplay(output,"\n<< Global Abort Done >>");
  if(output.getText().indexOf("ABORT DONE BY PARTICIPANT")<0)
	  appendDisplay(output,"\nABORT DONE BY PARTICIPANT \n");   
  
  

}
 else if(nt.equals("GLOBALCOMMITDONE"))
{
 String fromwhom=strT.nextToken();
 boolean flag=false;

  if(output.getText().indexOf("Global Commit Done")<0)
	appendDisplay(output,"\n<< Global Commit Done >>");
 
}	
else if(nt.equals("TIMELEFT"))
{
	 String fromwhom=strT.nextToken();
	 String answer=strT.nextToken();
 	if(Integer.parseInt(answer)>time && !invalid)
	{
	appendDisplay(output," << Global Abort Done >>");
	invalid=true;
	out.writeObject("GABORT#" + fromwhom + "#" +  question);	
 	}
	 //refreshTime(Integer.parseInt(answer));
}	
else if(nt.equals("GLOBALACK"))
{
	String fromwhom=strT.nextToken();
	String answer=strT.nextToken();
	appendDisplay(output," \n<< Global Acknowledgment By Coordinator Press With OK Button >>\r\n");
	//out.writeObject("GAACK#" + fromwhom + "#" +  answer);	
	ack.setEnabled(true);
	abort.setEnabled(false);
}	
  else if(nt.equals("GLOBALABORT"))
{
     String fromwhom=strT.nextToken();
     String answer=strT.nextToken();	
 }            
     else if(nt.equals("ANSWER"))
	{
	 String fromwhom=strT.nextToken();
	 String answer=strT.nextToken();
    if(!myname.equals(fromwhom))
	{
 	 if(answer.trim().startsWith("COMM"))
	{
		if(!answers.contains(fromwhom+"(P)"))
		{
	 	       answers.add(fromwhom + "(P)");	
		       responses.add(answer);
		}
	}
	 appendDisplay(output,fromwhom +  " = " + answer);
	if(answer.startsWith("AB"))
	{
 	appendDisplay(output," << Global Abort Done >>");
	fos=new FileOutputStream(name + "(P)"+".txt",true);
					pw=new PrintWriter(fos);		
					pw.println(query + "   " + name + "(P)" + "   ABORT ");
					pw.close();
					fos.close();
	out.writeObject("GABORT#" + fromwhom + "#" +  question);
 	}
 	else
	if(invalid)
	{
	appendDisplay(output," << Global Abort Done >>");
	fos=new FileOutputStream(name + "(P)"+".txt",true);
				pw=new PrintWriter(fos);		
				pw.println(query + "   " + name + "(P)" + "   ABORT ");
				pw.close();
				fos.close();
	out.writeObject("GABORT#" + fromwhom + "#" +  question);
	}
	else
	{
     if(answers.size()!=list.getModel().getSize()-2)
	{
	  end=System.currentTimeMillis(); 
	  appendDisplay(output,"\nWaiting For Other Clients To Give Vote \n");

	  //  invalid=true; 
	//  refreshTime(cnt);
 	}
		else
		{  
		 	invalid=false;
			ack.setEnabled(true);
		}		
}
}
 }
 else if(nt.equals("ACKCOMMIT"))
 {
	  String fromwhom=strT.nextToken();
	  String answer=strT.nextToken();
	  JOptionPane.showMessageDialog(this,myname +"  " + fromwhom);
//	 if(!myname.equals(fromwhom))
	//{
 	 if(answer.trim().startsWith("YES"))
	{
		if(!fanswers.contains(fromwhom+"(P)"))
		{
	 	       fanswers.add(fromwhom + "(P)");	
		       fresponses.add(answer);
		}
	}
	appendDisplay(output,fromwhom +  " = " + answer);
	if(fanswers.size()!=list.getModel().getSize()-2)
	{
			  appendDisplay(output,"\r\nWaiting For Other Clients To Give Acknowledgment Will Wait for 2Mins \r\n");
			  
             //FOR MAKING THREAD TO WAIT TO CHECK WHETHER THEY HAVE NOT GIVEN ACK
/*             try
			  {
			  if(myrole.equals("CO"))
				  {
				 for(int ackstart=10;ackstart>=1;ackstart--)
			      Thread.sleep(500);
				  if(ackstart!=-1)
					  alive=false;
				  else
					  alive=true;
				  
				  if(alive==false)
				  {
					  appendDisplay(output,"\r\nGLOBAL COMMIT DONE\r\n");
					  if(myrole.equals("CO"))
					  JOptionPane.showMessageDialog(this,"TimeOut is Done For Getting Acknowledgment So Globally Commit Done For the Transaction " + fanswers.size());
					  
		
					fos=new FileOutputStream(name + "(C).txt",true);
					pw=new PrintWriter(fos);		
					pw.println("Global Commit Done \n");
			for(int i=0;i<answers.size();i++)
				     pw.println(query + "   " + answers.get(i) + "   " + responses.get(i));
					pw.close();
					fos.close();
			
			for(int i=0;i<answers.size();i++)
				{
					fos=new FileOutputStream(answers.get(i)+".txt",true);
					pw=new PrintWriter(fos);		
					pw.println(query + "   " + answers.get(i) + "   " + responses.get(i));
					pw.close();
					fos.close();
				}
			 }
							  
			  }
			  catch(Exception ex)
			  {
				  appendDisplay(output,ex.getMessage());
			  }*/
	}
	else
	{
			appendDisplay(output,"\r\nGLOBAL COMMIT DONE\r\n");
			JOptionPane.showMessageDialog(this,"All have Given Commit Answers So Global Commit Done ");
			//ack.setEnabled(true);
					fos=new FileOutputStream(name + "(C).txt",true);
					pw=new PrintWriter(fos);		
					pw.println("Global Commit Done \n");
			for(int i=0;i<answers.size();i++)
				     pw.println(query + "   " + answers.get(i) + "   " + responses.get(i));
					pw.close();
					fos.close();
			
			for(int i=0;i<answers.size();i++)
				{
					fos=new FileOutputStream(answers.get(i)+".txt",true);
					pw=new PrintWriter(fos);		
					pw.println(query + "   " + answers.get(i) + "   " + responses.get(i));
					pw.close();
					fos.close();
			
				}
	}
	//}
 }
   else if(nt.equals("QUESTION"))
{
	 String fromwhom=strT.nextToken();
	 String answer=strT.nextToken();	
	if(query.trim().length()==0)
	query=answer;
	else
	{
		if(!query.equals(answer))	
			query=answer;	
	}
    	//if(!myname.equals(fromwhom))
	 //appendDisplay(output,fromwhom + " " + answer);
}
                 else if (nt.equals("SMILE2")){
                    String fromWho = strT.nextToken();
                    if (smileIcon.getIconHeight() > 0 && !isEmoticonDisabled){
                        appendDisplay(output, fromWho + ":" + "% %");
                        insertImage("smiley");
                    }
                    else
                        appendDisplay(output, fromWho+": " + "%:-)%");
                }

                else if (nt.equals("SAD2")){
                    String fromWho = strT.nextToken();
                    if (sadIcon.getIconHeight() > 0 && !isEmoticonDisabled){
                        appendDisplay(output, fromWho + ":" + "% %");
                        insertImage("sad");
                    }
                    else
                        appendDisplay(output, fromWho + ": " + "%:-(%");
                }

                else if (nt.equals("KILLER2")){
                    String fromWho = strT.nextToken();
                    if (killerIcon.getIconHeight() > 0 && !isEmoticonDisabled){
                        appendDisplay(output, fromWho + ":" + "% %");
                        insertImage("killer");
                    }
                    else
                        appendDisplay(output, fromWho + ": " + "%>:-)%");
                }

            }// end if(countTokens)

            else{
               if (!line.equals("<<SERVERSHUTDOWN>>")){
                    appendDisplay(output, line);
               }
            }

        }
        else if (objReceived instanceof Vector)
		{
			//if downloads are there activate download button else not required
 
          v =(Vector)objReceived;
          vectorList.setSize(v.size());
		  downloadcount=Integer.parseInt(v.get(0).toString());
		  //v.remove(0);
		  int idx=0;
		  int value=-1;
		 
          for(int i=1; i<v.size(); i++,idx++)
            vectorList.set(i-1,v.get(i));
          list.ensureIndexIsVisible(v.size());
    
    list.repaint();
	updateControls();
	
	answers=new ArrayList<String>();
 	responses=new ArrayList<String>();
	
	fanswers=new ArrayList<String>();
 	fresponses=new ArrayList<String>();
    count=3000;	
         }

//  ********************************************************************
//								B Y T E
//  ********************************************************************
				else if (objReceived instanceof byte[]){
  				final JFileChooser chooser = new JFileChooser();
	  			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          chooser.setSelectedFile(new File(fileName));
		  		chooser.addPropertyChangeListener(new PropertyChangeListener(){
  						public void propertyChange(PropertyChangeEvent pce){
	  						if(pce.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)){
                  chooser.setSelectedFile(new File(pce.getNewValue() + File.separator + fileName));
			  				}
              }
          }
				  );
  				option = chooser.showSaveDialog(Client.this.output);
          File f = chooser.getSelectedFile();
	  			if (option==JFileChooser.APPROVE_OPTION)
                                          {
  	  			try{
				    		FileOutputStream fout = new FileOutputStream(f);
					    	byte[] b = (byte[])objReceived;
						    fout.write(b);
  						  fout.close();
  	  			}catch (IOException ioe){
                System.out.println("Client:> error while receiving bytes");
                ioe.printStackTrace();
            }
     	  }
       }
    if(isvoted && !invalid)
	{
	   refreshTime(cnt);
	   isvoted=false;
	}
     }    
     while (!line.equals("<<SERVERSHUTDOWN>>"));
        appendDisplay(output, "Server Down\n");
        JOptionPane.showMessageDialog(this, "Error finding chat server.", "Connection error", JOptionPane.ERROR_MESSAGE);

    }
    catch (IOException ioe){
        System.out.println("Client:> error finding the chat server...");
        JOptionPane.showMessageDialog(this, "Error finding chat server.", "Connection error", JOptionPane.ERROR_MESSAGE);
    }
    catch (ClassNotFoundException cnfe){
        System.out.println("Client:> class not found exception");
        cnfe.printStackTrace();
        System.exit(0);
    }
 }
 private void refreshTime(int in)
{
	try	
	{
	if(output.getText().indexOf("Time Left ")<0)
  	appendDisplay(output,"Time Left " + ((time*1000)-(in*1000)) + "ms \n");
  	cnt=in;
	if(cnt>time)
	{
		invalid=true;
		cnt=0;
	}
	cnt++;
    if(!invalid)
	out.writeObject("TIME#"+name+"#"+cnt);
  	}
 	catch(Exception ex)
	{
	appendDisplay(output,"Error "  + ex.getMessage());
	}
}  	
    public String getServerName(){
        String serverName = null;
        System.out.println();
        Properties prop = new Properties(System.getProperties());
        try{
            FileInputStream fin = new FileInputStream("client.properties");
            prop.load(fin);
            serverName = prop.getProperty("servername");
        }
        catch(IOException ioe){
            System.out.println("Client:> exception at getServerName()\n" + ioe.getLocalizedMessage());
            return null;
        }
        return serverName;
    }

    public void appendDisplay(JTextPane display, String text){

            StyledDocument doc = (StyledDocument)display.getDocument();
            Style style = doc.addStyle("newStyle", null);

            StringTokenizer strTok = new StringTokenizer(text, "%");

            if (strTok.countTokens() > 1){
                MutableAttributeSet attr = new SimpleAttributeSet();
                String strFirstElement = strTok.nextToken();
                String strSecondElement = strTok.nextToken();
                String strName = strFirstElement + "\n";
                String strMessage = "   " + strSecondElement + "\n";
                doc = (StyledDocument)display.getDocument();
                try{
                    doc.insertString(doc.getLength(), strName, style);
                    output.setCaretPosition(doc.getLength());
                }
                catch(BadLocationException ble){
                    System.out.println("server>: bad location error");
                }
                display.setDocument(doc);

                doc = (StyledDocument)display.getDocument();
                StyleConstants.setForeground(attr, new Color(1, 0, 0));
                doc.setCharacterAttributes(doc.getLength() - strName.length(), strName.length(), attr, true);
                if (!strSecondElement.trim().equals("")){
                    try{
                        doc.insertString(doc.getLength(), strMessage, style);
                        output.setCaretPosition(doc.getLength());
                    }
                    catch(BadLocationException ble){
                        System.out.println("server>: bad location error");
                    }
                }
                display.setDocument(doc);
            }
            else{
                try{
                    doc.insertString(doc.getLength(), text, style);
                    output.setCaretPosition(doc.getLength());
                }
                catch(BadLocationException ble){
                    System.out.println("server>: bad location error");
                }
                display.setDocument(doc);
            }
        }

    public void insertImage(String description){
        StyledDocument doc = (StyledDocument)output.getDocument();
        Style style = doc.addStyle("StyleName", null);

        if (description.equals("killer")){
            try{
                doc.insertString(doc.getLength(), "   ", style);
                StyleConstants.setIcon(style, killerIcon);
                doc.insertString(doc.getLength(), ">:)\n", style);
                output.setCaretPosition(doc.getLength());
            }
            catch(BadLocationException ble){
                System.out.println("Bad location");
            }
        }

        else if (description.equals("sad")){
            try{
                doc.insertString(doc.getLength(), "   ", style);
                StyleConstants.setIcon(style, sadIcon);
                doc.insertString(doc.getLength(), ":-(\n", style);
                output.setCaretPosition(doc.getLength());
            }
            catch(BadLocationException ble){
                System.out.println("Bad location");
            }
        }

        else if (description.equals("smiley")){
            try{
                doc.insertString(doc.getLength(), "   ", style);
                StyleConstants.setIcon(style, smileIcon);
                doc.insertString(doc.getLength(), ":-)\n", style);
                output.setCaretPosition(doc.getLength());
            }
            catch(BadLocationException ble){
                System.out.println("Bad location");
            }
      }
    }

        public static void main(String[] args)throws IOException{
            Client client = new Client(args[0]);
            client.doLogin();
	}
}



