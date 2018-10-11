package gridGame;

//Name: Pratyank Bhosale
//Date: May 31, 2017
//Game:	Hangman for Computer Science terms

//imports the libraries needed
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;

//class for game
public class gridGame extends Applet implements ActionListener
{	
	//for music
	AudioClip soundFile;
	
	//This declares a static final serialVersionUID field of type long
	private static final long serialVersionUID = -5311229090373562691L;

	//screens used
	Panel p_screen;
	Panel screen1, screen2, screen3, screen4;
	CardLayout cdLayout = new CardLayout ();

	//String of the alphabet (this is used to see if the user even enters in a letter, and used to draw buttons)
	String al="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	//array of jLabels for the alphabet displayed
	JLabel ALPHA [] = new JLabel [26];

	//the buttons displayed on the game screen
	JButton reset = new JButton ("Reset");
	JButton save = new JButton ("Save");
	JButton open = new JButton ("Open");
	JButton submit = new JButton ("Check");
	JButton hint = new JButton ("hint");

	//default message at the bottom on the closing screen (the msg changes when you win)
	JLabel conT = new JLabel ("for downloading this game, to become more fluent with computer science terms");
	JLabel hangM;
	JLabel ANSlB []= new JLabel[15];
	JLabel score;

	boolean used = false;//boolean for whether or not the user has used a hint
	boolean won = false;/*boolean for whether or not the user has won 
	(the point of this is so they can continue the game with their current score, and so the closing screen msg changes after
	they win */

	//textfield for the user to enter in a guess
	JTextField answer;
	
	String guess = "";//stores the guesses the user makes for a word

	String ansText;//gets textfield in actionPerformed
	String chosen;//for getting action command in actionPerformed

	//3d array for levels, terms, and hint
	String CSterms [][][] = { { {"method","object","widget"},
		{"just a chunk of code that does a particular job, over and over again when called","an instance of a class","the part of a GUI that allows the user to interface with an application"} },
		{{"mutator","constructor","accessor"}, 
			{"A method used to customize objects, they are also widely known as setter methods","like a method, no return type, and the name of this must be the same as the name of the class","a method that gets an objects values/properties"} },
			{{"instantiation", "declaration","facilatator"},
				{"The creation of a particular  instance an object or type","sets aside Ram for instance of an object","perform fuctions on objects, and isnt a mutator, a constructor, or an accessor"} }};
	
	String lastWord = "";//stores the users last word
	String wrongLet = "";//stores the wrong letters guessed
	String wordChosen;// = CSterms[rand];

	int rand;//variable that stores the position of the random word

	int amountGT = 0;//amount of letters guessed

	int occur = 0;//for possible occurences of one letter in a word
	int hasOcc = 0;//double letters guessed
	int prev = 0;//helps with multiple double letters

	int lv = 0;//for the level the user in on
	int h = 1;//to help code be more manreadable h (short for hint) is set to 1 (position in 3d array)
	int w = 0;//to help code be more manreadable w (short for word) is set to 0 (position in 3d array)

	//rows and columns for the letters to be guess or were guessed to be displayed on the screen
	int row=2;
	int col = 13;

	int lives=6;//lives the user has per word
	int n=0;//score the user begins with

	//init method
	public void init ()
	{
		//panel for all the screens to exist within
		p_screen = new Panel ();
		p_screen.setLayout (cdLayout);
		
		//screens are called and set up
		splash ();
		instructions ();
		game ();
		closing ();
	
		resize (655, 650);//resizes window
		setLayout (new BorderLayout ());
		add ("Center", p_screen);//adds the main panel
		
		//for music
		soundFile = getAudioClip (getDocumentBase (), "music.wav");
		soundFile.loop ();
	}
	
	//method for starting screen
	public void splash ()
	{ 
		//sets up the starting screen
		screen1 = new Panel ();//creates the screen
		screen1.setBackground (new Color (0,220,255));//sets a blue type background
		
		//titles
			//created	
		JLabel title = new JLabel ("WELCOME TO THE");
		JLabel title2 = new JLabel ("COMPUTER SCIENCE HANGMAN GAME!!");
			//customized
		title.setFont(new Font ("Bradley Hand ITC", Font.BOLD,30));
		title.setForeground(Color.red);
		title2.setFont(new Font ("Bradley Hand ITC", Font.BOLD,30));
		title2.setForeground(Color.red);

		//button to proceed to the instructions screen
			//created
		JButton next = new JButton ("Begin!");
			//customized
		next.setPreferredSize (new Dimension (250, 50));
		next.setBackground (new Color (228, 0, 120));
		next.setForeground(Color.white);
		next.setFont(new Font ("Bradley Hand ITC", Font.BOLD,30));
			//set a command for this button
		next.setActionCommand ("enter");
		next.addActionListener (this);//and listen to it

		JLabel bg = new JLabel (createImageIcon ("canvas.png"));//big picture
		
		//add everything to the screen
		screen1.add (title);
		screen1.add (title2);
		screen1.add(bg);
		screen1.add (next);
		p_screen.add ("1", screen1);
	}
	
//instructions screen method
	public void instructions ()
	{
		//screen is
		screen2 = new Panel ();//made
		screen2.setBackground (new Color (0,220,255));//and gets a blue type background

		//Title and blank jLabels are created 
		JLabel title = new JLabel ("                        Instructions                        ");
		JLabel blank = new JLabel ("                                                                                                                                                                ");

		//title is customized
		title.setForeground(Color.red);
		title.setFont (new Font ("Arial", Font.PLAIN, 25));

		//Button to get to the game
		JButton game = new JButton ("Game");
		//action command and action listener
		game.setActionCommand ("s3");
		game.addActionListener (this);
		//customizes game button
		game.setPreferredSize (new Dimension (200, 50));
		game.setBackground (new Color (228, 0, 120));
		game.setForeground (Color.white);
		game.setPreferredSize (new Dimension (220, 30));
		game.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		
		//button to get back to the start screen
		JButton back = new JButton ("Back");
		//action command and action listener
		back.setActionCommand ("s1");
		back.addActionListener (this);
		//customizes the back button
		back.setPreferredSize (new Dimension (200, 50));
		back.setBackground (new Color (228, 0, 120));
		back.setForeground (Color.white);
		back.setPreferredSize (new Dimension (220, 30));
		back.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		
		//blank jlabel to help the appearance look better & jlabels for instructions
		JLabel blank1 = new JLabel ("                                                                                             ");
		JLabel ins1 = new JLabel("This game is a Hangaman game for cs (computer science) terms, the point of this game is ");
		JLabel in1_5=new JLabel ("help you become more fluent with cs terms. In order to play this game, you must enter in");
		JLabel ins2 = new JLabel("a single letter or whole word, wrong guesses (for single letters) will result in a deduction");
		JLabel ins2_5 = new JLabel("in points, the same applies to the use of a hint. You also begin with 6 lives, and the");
		JLabel ins3 = new JLabel ("lives reset after you move on to your next word. Also after you reach 50 points, you win!!");
		//blank jlabel to help the appearance look better & jlabels for terms to know
		JLabel blank2 = new JLabel ("                                                                                                                                                                                                            ");
		JLabel termsT = new JLabel ("Terms to know: ");
		termsT.setForeground(Color.red);
		termsT.setFont (new Font ("Arial", Font.PLAIN, 20));
		JLabel blank3 = new JLabel ("                                                                                                                                                                         ");

		//array of jlabels for the words and hints in each level
		JLabel [] terms = new JLabel [CSterms.length*CSterms[0][w].length*2];
		Panel bgs = new Panel (new GridLayout(CSterms.length*CSterms[0][w].length*2,2));
		Panel ssg [] = new Panel [CSterms.length*CSterms[0][w].length*2*2];
		
		//a for loop to add the terms and their definitions to the grid
		int counter = 0;
		for (int x = 0; x < 3; x++){
			for (int y = 0; y<3;y++){
				terms[counter] = new JLabel(CSterms[x][w][y]+": "+CSterms[x][h][y]);	
				terms[counter].setFont (new Font ("Comic Sans MS", Font.PLAIN, 13));
				ssg[counter] = new Panel();
				ssg[counter].add(terms[counter]);
				bgs.add(ssg[counter]);
				counter++;
			}
		}

		//sets the font for all the instruction jlabels 
		ins1.setFont (new Font ("Comic Sans MS", Font.PLAIN, 13));
		in1_5.setFont (new Font ("Comic Sans MS", Font.PLAIN, 13));
		ins2.setFont (new Font ("Comic Sans MS", Font.PLAIN, 13));
		ins2_5.setFont (new Font ("Comic Sans MS", Font.PLAIN, 13));
		ins3.setFont (new Font ("Comic Sans MS", Font.PLAIN, 13));

		//adds everything to the screen
		screen2.add (title);
		screen2.add(blank);
		screen2.add (game);
		screen2.add (back);
		screen2.add(blank1);
		screen2.add(ins1);
		screen2.add(in1_5);
		screen2.add(ins2);
		screen2.add(ins3);
		screen2.add(blank2);
		screen2.add(termsT);
		screen2.add(blank3);
		screen2.add(bgs);
		p_screen.add ("2", screen2);
	}
	//game method
	public void game ()
	{ 
		//screen3 is created and gets a background color
		screen3 = new Panel ();
		screen3.setBackground (new Color (0,220,255));
		
		//random word gets chosen
		rand = (int )(Math.random() * CSterms[lv][w].length);
		wordChosen = CSterms[lv][w][rand];

		//game screen title & subtitle
		JLabel TITLE = new JLabel (createImageIcon ("title.gif"));
		JLabel title = new JLabel ("                                     FOR   CS   TERMS!!!");
		
		//change color & font of subtitle
		title.setForeground(Color.yellow);
		title.setFont (new Font ("Broadway", Font.PLAIN, 24));

		//closing screen button
		JButton closing = new JButton ("Closing Screen");
		//action command and listener
		closing.setActionCommand ("s4");
		closing.addActionListener (this);
		//customizes closing screen
		closing.setBackground (new Color (228, 0, 120));
		closing.setForeground (Color.white);
		closing.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		closing.setPreferredSize (new Dimension (220, 30));

		//instructions screen button
		JButton instr = new JButton ("Instructions");
		//action command and action listener
		instr.setActionCommand ("enter");
		instr.addActionListener (this);
		//customizes instructions button
		instr.setBackground (new Color (228, 0, 120));
		instr.setForeground (Color.white);
		instr.setPreferredSize (new Dimension (220, 30));
		instr.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		
		//Jlabel beside jtextfield
		JLabel enterHere = new JLabel("Enter Your Guess Here: ");
		enterHere.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		
		n = 0;//sets score to 0
		score = new JLabel ("                                                                             Score : "+n);//creates a score jLabel
		score.setFont(new Font ("Bradley Hand ITC", Font.BOLD,15));//sets font for score jlabel
		answer = new JTextField(18);//sets textfield size

		//sets up the submit
		submit.setBackground (new Color (228, 0, 120));
		submit.setForeground (Color.white);
		submit.setPreferredSize (new Dimension (90, 20));
		submit.setActionCommand ("submit");
		submit.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		submit.addActionListener (this);

		//sets up hint button
		hint.setBackground (new Color (228, 0, 120));
		hint.setForeground (Color.white);
		hint.setPreferredSize (new Dimension (80, 20));
		hint.setActionCommand ("GET_HINT");
		hint.addActionListener (this);
		hint.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));

		//sets up reset button
		reset.setBackground (new Color (228, 0, 120));
		reset.setForeground (Color.white);
		reset.setPreferredSize (new Dimension (90, 20));
		reset.setActionCommand ("reset");
		reset.addActionListener (this);
		reset.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		
		//sets up save button
		save.setBackground (new Color (228, 0, 120));
		save.setForeground (Color.white);
		save.setPreferredSize (new Dimension (90, 20));
		save.setActionCommand ("save");
		save.addActionListener (this);
		save.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));

		//sets up open button
		open.setBackground (new Color (228, 0, 120));
		open.setForeground (Color.white);
		open.setPreferredSize (new Dimension (90, 20));
		open.setActionCommand ("open");
		open.addActionListener (this);
		open.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));

		//disables hint
		hint.setEnabled(false);
		//disables reset
		reset.setEnabled(false);
		//puts up a hangman stand
		hangM = new JLabel(createImageIcon ("0.png"));

		//panels to organize the screen
		Panel grid=new Panel(new GridLayout(row,col));

		Panel A = new Panel (new GridLayout (2, 2));
		Panel b  = new Panel();
		Panel c  = new Panel();

		Panel newGride = new  Panel(new GridLayout(1,20));
		// loop to create jlabels before they are even used (program cashes if this isnt used)
		for (int i =0; i<15;i++){
			ANSlB[i] = new JLabel(" ");
			ANSlB[i].setPreferredSize(new Dimension(49,20));
			ANSlB[i].setForeground (Color.black);
			ANSlB[i].setFont(new Font ("Bradley Hand ITC", Font.BOLD,14));
			newGride.add(ANSlB[i]);

		}
		//draws underscores for the amount of letters of the word
		for (int i =0; i<wordChosen.length();i++){
			ANSlB[i].setText("_");
			ANSlB[i].setPreferredSize(new Dimension(49,20));
			ANSlB[i].setForeground (Color.black);

			newGride.add(ANSlB[i]);
		}

		//adds spaces after so that the word is centered properly for all levels
		for (int y = wordChosen.length(); y<15; y++){
			System.out.println(y);
			ANSlB[y].setText(" ");
			ANSlB[y].setPreferredSize(new Dimension(49,20));
			newGride.add(ANSlB[y]);
		}
		//sets background
		grid.setBackground (new Color (228, 0, 120));
		
		//adds alphabet to the grid
		int counter = 0;
		for(int i=0; i<row; i++){
			for(int j=0; j<col; j++){
				
				ALPHA[counter]= new JLabel ("       "+al.substring(counter, counter+1)/*alpha[counter]*/);
				ALPHA[counter].setPreferredSize(new Dimension(49,20));
				ALPHA[counter].setForeground (Color.white);
				ALPHA[counter].setFont(new Font ("Bradley Hand ITC", Font.BOLD,13));
				grid.add(ALPHA[counter]);
				counter++;
			}
		}

		//adds everything to the screen
		
		screen3.add (TITLE);
		screen3.add (title);
		screen3.add (closing);
		screen3.add (instr);
		screen3.add(score);
		screen3.add(hangM);

		newGride.setPreferredSize (new Dimension (500,30));
		screen3.add(newGride);

		b.add(enterHere);
		b.add(answer);
		b.add(submit);
		A.add(b);

		screen3.add(grid);
		c.add (hint);	
		c.add (reset);
		c.add (save);
		c.add (open);
		A.add(c);
		screen3.add(A);
		p_screen.add ("3", screen3);

	}
	//method for closing screen
	public void closing ()
	{ 
		//creates screen
		screen4 = new Panel ();
		screen4.setBackground (new Color (0,220,255));
		JLabel title = new JLabel ("                    Closing Screen                    ");

		//creates blank jlables to make appearance look better
		JLabel blank = new JLabel ("                                                                                                                                                                ");
		JLabel blank2 = new JLabel ("                                                                                                                                                                ");
		JLabel blank3 = new JLabel ("                                                                                                                                                                ");
		JLabel blank4 = new JLabel ("                                                                                                                                                                ");
		JLabel blank5 = new JLabel ("                                                                                                                                                                ");
		JLabel blank6 = new JLabel ("                                                                                                                                                                ");
		JLabel blank7 = new JLabel ("                                                                                                                                                                ");
		JLabel blank8 = new JLabel ("                                                                                                                                                                ");
		JLabel blank9 = new JLabel ("                                                                                                                                                                ");

		//makes the title look good
		title.setForeground(Color.red);
		title.setFont (new Font ("Arial", Font.PLAIN, 24));
		
		//makes the return button
		JButton returnBack = new JButton ("Return to Game");
		//makes it look good
		returnBack.setPreferredSize (new Dimension (225, 45));
		returnBack.setBackground (new Color (228, 0, 120));
		returnBack.setForeground(Color.white);
		returnBack.setFont(new Font ("Bradley Hand ITC", Font.BOLD,20));
		//sets the command of the button, and listens to it
		returnBack.setActionCommand ("s3");
		returnBack.addActionListener (this);
		
		//creates all the credits and formats it
		JLabel credTitle = new JLabel ("                                 Credits                                 ");
		credTitle.setFont (new Font ("Bradley Hand ITC", Font.BOLD,20));
		credTitle.setForeground(Color.red);

		JLabel cred1 = new JLabel ("                                 Programmer: Pratyank Bhosale                                 ");
		cred1.setFont (new Font ("Bradley Hand ITC", Font.BOLD,15));
		cred1.setForeground(Color.red);

		JLabel cred2 = new JLabel ("                                 Alpha Tester: Vraj                                 ");
		cred2.setFont (new Font ("Bradley Hand ITC", Font.BOLD,15));
		cred2.setForeground(Color.red);

		JLabel cred3 = new JLabel ("                                 Beta Tester: Jubliee                                 ");
		cred3.setFont (new Font ("Bradley Hand ITC", Font.BOLD,15));
		cred3.setForeground(Color.red);

		JLabel cred4 = new JLabel ("                                 Pogramming Advisor: Ms. Gorski                                 ");
		cred4.setFont (new Font ("Bradley Hand ITC", Font.BOLD,15));
		cred4.setForeground(Color.red);

		JLabel cred5 = new JLabel ("                                 Image editor: Pratyank Bhosae & Image Source: Google images                                 ");
		cred5.setFont (new Font ("Bradley Hand ITC", Font.BOLD,15));
		cred5.setForeground(Color.red);

		JLabel con =  new JLabel (createImageIcon ("cc.png"));//adds the congratulations pic

		//formats the msg at the bottom
		conT.setFont (new Font ("Bradley Hand ITC", Font.BOLD,15));
		conT.setForeground(Color.red);
		
		//adds everything to the screen
		screen4.add (title);
		screen4.add(blank);
		screen4.add (returnBack);
		screen4.add(blank2);
		screen4.add(credTitle);
		screen4.add(blank3);
		screen4.add(cred1);
		screen4.add(blank4);
		screen4.add(cred2);
		screen4.add(blank5);
		screen4.add(cred3);
		screen4.add(blank6);
		screen4.add(cred4);
		screen4.add(blank7);
		screen4.add(cred5);
		screen4.add(blank8);
		screen4.add(con);
		screen4.add(blank9);
		screen4.add(conT);
		p_screen.add ("4", screen4);
	}
	
	//action performed method (whenever something is clicked, we go here) 
	public void actionPerformed (ActionEvent e)
	{ 
		chosen = e.getActionCommand ();//gets the action command as a string

		//if the user choose to save
		if (chosen.equals("save")){
			String user = JOptionPane.showInputDialog ("Please enter your name");//prompt them for their name
			if (user!=null)//if they dont click cancel or save nothing as their name
				save(user);//save the file
		}
		
		//else if the user choose to op
		else if (chosen.equals("open")){
			String user = JOptionPane.showInputDialog ("Please enter your name");//prompt them for their name
			if (user!=null)//if they dont click cancel or open nothing as their name
				open(user);//open the file
		}
		//else if screen 1
		else if (chosen.equals ("s1"))
			cdLayout.show (p_screen, "1");//open splash screen
		//else if screen 2
		else if (chosen.equals ("enter"))
			cdLayout.show (p_screen, "2");//open instructions screen
		//else if screen 3
		else if (chosen.equals ("s3"))
			cdLayout.show (p_screen, "3");//open game screen
		//else if screen 4
		else if (chosen.equals ("s4"))
			cdLayout.show (p_screen, "4");//open closing screen
		//else if reset is clicked
		else if (chosen.equals("reset")){
			n=0;//set the score to 0
			//update the score on the screen
			score.setText("                                                                             Score : "+n);
			MOVEON();//move on to a new word
		}
		//else if hint is clicked
		else if (chosen.equals("GET_HINT")){
			hint();//call the hint method
		}
		// else if submit is clicked
		else if (chosen.equals("submit")){
			
			if (n==0)//if score is 0
				reset.setEnabled(false);//disable reset
			else//else
				reset.setEnabled(true);//enable it
			
			ansText = answer.getText().toLowerCase();//gets the input on the jtextfield
			
			//if the user guesses the word
			if (ansText.equals(wordChosen)){
				//increase the score by the amount of letters - the letters already guessed
				n += (wordChosen.length() - amountGT);
				score.setText("                                                                             Score : "+n);
				MOVEON();//then move on to the next word
			}
			//else if the length is 1
			else if (ansText.length()==1)
			{
				
				boolean got = false;//make got set to false (this is for whether or not the user got a word in the word that was chosen
				hasOcc = 0;//set to 0

				//this part determines the amount occurrences
				if (prev<=wordChosen.length()){
					occur=0;
					for (int q = 0; q<wordChosen.length();q++){
						if (ansText.equals(wordChosen.substring(q,q+1))&&guess.indexOf(ansText)==-1){//and this if makes 
							//sure that user cant guess the same right letter twice
							occur++;
						}
					}
				}

				//this part determines whether or not the user guessed right
				for (int i = 0; i< wordChosen.length(); i++){
					if (hasOcc < occur && ansText.equals(wordChosen.substring(i,i+1)) || ansText.equals(wordChosen.substring(i,i+1)) && guess.indexOf(ansText)==-1 )
					{//if they do 
						//put up the letter they guessed in the place of a dashed arrow
						ANSlB[i].setText(ansText);
						amountGT++;//increase the amount of letters guessed
						CORRECT();//call the correct method
						guess += ansText;//add the guess to the string of guesses made
						got = true;//set got to true
						hasOcc++;//increase the amount that has occurred
					} 
				}
				prev++;//Increase prev

				//this part determines whether or not the user entered in a wrong guess another time
				for (int i=0;i<wrongLet.length();i++){
					if (ansText.toUpperCase().equals(wrongLet.substring(i, i+1)))
					{
						ansText="1";//and test ansText to 1
						break;//then exits the loop
					}
				}

				//if the guessed letter isnt in the alphabet, is empty or null 
				if ((al.indexOf(ansText)== -1) || answer.getText().isEmpty() || answer.getText() == null){
					//then tell the user to enter in proper input, and not to enter in letters that were already guessed
					dialog("Enter letters only please, and dont enter letters that were already guessed");
					ansText="";//sets ansText to nothing
				}
				
				//else if the user just completed the word
				else if (amountGT == wordChosen.length()){
					MOVEON();//move on to the next word
				}
				//else if the user guessed right
				else if (got == true){
					showStatus("Correct");//show that they guessed right on showStatus (too many pop ups can be annoying)
				}
				
				//else if the user entered in a  right guess
				else if (guess.indexOf(ansText)!=-1){
					dialog("you already entered this letter");//tell them they already entered this
				}
				//else if they just guessed wrong, 
				else if (got == false) 
					INCORRECT(--lives);//call the incorrect method, and reduce lives

			}
			//else
			else{
				
				boolean notVal = true;//notVal equal to true
				int hasL = 0;//set hasL to 0 (has letters)

				//this part find out the amount of letters in the answer guessed
				for (int i=0; i<ansText.length(); i++){
					if (ansText.substring(i, i+1).indexOf(al)!= -1)
						hasL++;}

				//this part finds out if the answer is not in the alphabet
				for (int i=0;i<ansText.length();i++){
					if (al.indexOf(ansText.substring(i,i+1))== -1){
						notVal = false;
						break;
					}}

				//if the amount of letters guessed doesnt equals the amount of letters in the word
				if (hasL!= ansText.length() )
					//tell them to try again
					dialog("Try again...(with the right amount of letters, and use letters only) or just take a better guess :3");

				else if (ansText.length()==0||ansText.length()==wordChosen.length())//else if they enter in an empty string or if they have the right amount of letter (but wrong word)
					dialog("Guess again");//tell them to guess

				else if (notVal == true){//else if tell them to try again
					dialog("try again..");
				}

				else//else (when something that unexpected happens)
				{
					dialog("oops, try again :(");//tell the user to try again
				}
			}
			answer.setText("");//clear the textfield (so the user doesnt have to backspace after a guess)
		}
	}
	
	//incorrect method
	public void INCORRECT (int lost){
		//if score is greater than 0
		if (n>0){
			//reduce it, and update the score on the screen
			n--;
			score.setText("                                                                             Score : "+n);
		}
		//else 
		else{
			//disable hint and reset
			hint.setEnabled(false);
			reset.setEnabled(false);
		}
		
		//if the guess made by the user is 1 letter
		if (ansText.length()==1)
			wrongLet+=ansText.toUpperCase();//add the guess to the string of wrong letters, in upper case

		int getRid = al.indexOf(ansText.toUpperCase());//find the place of the wrong letter in the alphabet, and store it
		
		ALPHA[getRid].setForeground(Color.black);//then turn the corresponding letter in the alphabet black
		
		if (lives > 0)//if lives is bigger than 0
			hangM.setIcon(createImageIcon (6-lives+".png"));//add another body part to the screen (change pics)
		else {
			//show last hangman pic
			hangM.setIcon(createImageIcon ("6.png"));
			JOptionPane.showMessageDialog (null, "Word was: "+CSterms[lv][w][rand],"Correction",JOptionPane.INFORMATION_MESSAGE);
			MOVEON();//move on to the next word
		}	
	}

	//correct method
	public void CORRECT (){
		n++;//increase score
		//and update it
		score.setText("                                                                             Score : "+n);
		//enable hint and reset
		hint.setEnabled(true);
		reset.setEnabled(true);
	}

	//hint method
	public void hint (){
		//if the user already asked for hint (they forgot it) for the same word
		if (used==true)
			//show them the hint
			JOptionPane.showMessageDialog (null, CSterms[lv][h][rand],"Hint",JOptionPane.INFORMATION_MESSAGE);
		//else if their score is above 0
		else if (n>=1)
		{
			//reduce it & and update the score on the screen
			n--;
			score.setText("                                                                             Score : "+n);
			//show the user the hint
			JOptionPane.showMessageDialog (null, CSterms[lv][h][rand],"Hint",JOptionPane.INFORMATION_MESSAGE);
			//make used set to true, so they can use this hint again for this word
			used = true;
		}
	}

	//move on to the next word (MOVEON) method
	public void MOVEON (){
			
		//if the user wins
		if (n>=50&&won==false){
			conT.setText("for becoming an expert on computer science terms");//Congratulate them
			cdLayout.show (p_screen, "4");//take them to the closing screen
			won=true;//set won to true
		}
		//update the last word
		lastWord = wordChosen;
		
		//reset all letters in the panel
		for (int z = 0; z<wrongLet.length();z++){
			ALPHA[al.indexOf(wrongLet.substring(z,z+1))].setForeground(Color.white);
		}
		//reset variables
		used = false;
		wrongLet="";
		lives = 6;
		hangM.setIcon(createImageIcon ("0.png"));//and pictures
		prev = 0;
		
		//remove all the dashed lines
		for (int i =0; i<wordChosen.length();i++)
			ANSlB[i].setText ("");
		

		if (n>20){//if score is 20
			//test them on all words
			lv = (int)(Math.random() * CSterms[lv][w].length);
			
		}
		else if (n>=15){//if score is 15 or higher
			lv=2;//test them on level 3 words
				
		}
		else if (n>=10){//if score is 10 or high 
			lv=1;//test them on level 2 words
				
		}
		else{//else
			lv=0;//test them on level 1 words
			
		}
		randC();//call the random word choosing method
		
		amountGT = 0;//amount of letters guessed

		//draws underscores
		for (int i =0; i<wordChosen.length();i++){
			ANSlB[i].setText ("_");
			ANSlB[i].setPreferredSize(new Dimension(49,20));
			ANSlB[i].setForeground (Color.black);
			System.out.println("::"+i+" ");
		}
		
		//wastes time (a little)
		for (int q = 0; q<wordChosen.length();q++){
			if (ansText.equals(wordChosen.substring(q,q+1))){
				occur++;
			}
		}
		
		//set values to their orginal state so they can be used again for a new word
		guess = "";
		occur =0;//for multiple occurrences of one letter in a word//set it to 0
		hasOcc = 0;	//set to 0
	}

	//save method	
	public void save(String username){
		//array for int, string and boolean values
		int values1 [] = {lives,prev,n,amountGT,occur,hasOcc};
		String values2 [] = {lastWord,wordChosen,wrongLet,guess};
		boolean values3 [] = {used,won}; 
		PrintWriter out;
		//try to save these arrays
		try {
			out = new PrintWriter (new FileWriter (username));

			for (int i = 0; i < values1.length; i++){
				out.println(""+values1[i]);
			}
			for (int i = 0; i < values2.length; i++){
				out.println(""+values2[i]);
			}
			
			out.println(""+values3[0]);
			out.println(""+values3[1]);
			out.close ();
		}
		//if cant be save for some reason
		catch (IOException e) {
			dialog ("Error opening file " + e);//show an error msg and the type of error
		}
	}

	//open method
	public void open (String username){
		//give an int array, string array and boolean array defalut values
		int values1 [] = {0,0,0,0,0,0}; String values2[] = {"","","",""}; boolean values3 []={ false,false};
		// try read the values from the file
		BufferedReader in;
		try {
			in = new BufferedReader (new FileReader (username));
			String input = in.readLine ();

			for (int i = 0; i < 6; i++){
				values1[i]= Integer.parseInt (input);
				input = in.readLine ();
			}

			//input = in.readLine ();
			System.out.println("::::::::::::::::::"+input);
			for (int i = 0; i < 4; i++){
				values2[i]= input;
				input = in.readLine ();
			}
					
			values3[0]=Boolean.valueOf(input);
			values3[1]=Boolean.valueOf(in.readLine());
			in.close ();
			reDraw(values1, values2, values3);
		}
		//if cant be read for some reason
		catch (IOException e) {
			dialog("Error opening file "+ e);//show the error and error type
		}
	}
	
	//redraw method
	public void reDraw(int values1 [], String values2[], boolean values3[]){

		//gets rid of dashed lines
		for (int i =0; i<wordChosen.length();i++)
			ANSlB[i].setText ("");
		
		//set all variables the the right values
		used = values3[0];
		won = values3[1];
		lastWord = values2[0]; wordChosen = values2[1]; wrongLet = values2[2]; guess=values2[3];
		lives = values1[0]; prev = values1[1]; n = values1[2]; amountGT = values1[3]; occur = values1[4]; hasOcc = values1[5];
		
		//enables/diables reset and hint based off of score
		if (n==0){
			hint.setEnabled(false);
			reset.setEnabled(false);
		}
		else{
			hint.setEnabled(true);
			reset.setEnabled(true);	
		}
		
		//updates score for the current user
		score.setText("                                                                             Score : "+n);
		//lives shown in terms on body parts
		hangM.setIcon(createImageIcon (6-lives+".png"));
		
		//dashed lines for word chosen
		for (int i =0; i<wordChosen.length();i++){
			ANSlB[i].setText ("_");
			ANSlB[i].setPreferredSize(new Dimension(49,20));
			ANSlB[i].setForeground (Color.black);
			System.out.println("a"+i);
		}
		
		//finds the placement of the hint
		for (int i = 0; i < CSterms.length; i++){
			for (int y = 0; y < CSterms[i][w].length;y++){
				if (wordChosen.equals(CSterms[i][w][y])){
					rand = y;
					break;
					}
			}
		}
		
	
		//letters over the dashed lines
		for (int i=0; i < guess.length(); i++){
			for (int z = 0; z < wordChosen.length(); z++){
				if (guess!=""&& guess.charAt(i)==wordChosen.charAt(z)){
					ANSlB[wordChosen.indexOf(guess.substring(i, i+1))].setText (wordChosen.substring(z,z+1));
				}
			}
		}
		
		//wrong letters
		for (int z = 0; z<wrongLet.length();z++){
			ALPHA[al.indexOf(wrongLet.substring(z,z+1))].setForeground(Color.black);
		}
	}

	public void randC (){
		rand = (int )(Math.random() * CSterms[lv][w].length);
		System.out.println(CSterms[rand]);
		wordChosen = CSterms[lv][w][rand];
		System.out.println(wordChosen+": wordChosen");
	}

	public void emptyAns(){
		for (int i =0; i<15;i++){
			ANSlB[i] = new JLabel ("_");
			ANSlB[i].setPreferredSize(new Dimension(49,20));
			ANSlB[i].setForeground (Color.black);

		}

		for (int i =0; i<15;i++){
			ANSlB[i] = new JLabel ("");
			ANSlB[i].setPreferredSize(new Dimension(49,20));
			//ANSlB[i].setForeground (Color.black);

		}
	}

	//dialog method //used for alerts
	public void dialog(String s){
		//show a pop up for the string parameter when called 
		JOptionPane.showMessageDialog (null, s,"ALERT",JOptionPane.ERROR_MESSAGE);
	}

	//method for adding images to the screen
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = gridGame.class.getResource (path);
		if (imgURL != null)
		{
			return new ImageIcon (imgURL);
		}
		else
		{
			System.err.println ("Couldn't find file: " + path);
			return null;
		}
	}
}