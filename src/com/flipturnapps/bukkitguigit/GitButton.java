package com.flipturnapps.bukkitguigit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;

public class GitButton extends JButton implements ActionListener
{

	public static final String TXT_PULL = "Pull";
	public static final String TXT_CHECKOUT = "Checkout";
	public static final String TXT_GET_CHOICES = "Branches";
	public static final String TXT_CLONE_NEW = "Clone New";
	public static final String TXT_FIND_REPO = "Find Repo";
	private int stageId;
	private Executor executor;
	private static ArrayList<GitButton> list;
	private static int staticId;

	public GitButton(String string, int stageId, Executor exe) 
	{
		super(string);
		this.addActionListener(this);
		this.stageId = stageId;
		this.decideIfEnabled();
		if(list == null)
			list = new ArrayList<GitButton>();
		list.add(this);
		
		this.executor = exe;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(this.getText() == TXT_FIND_REPO)
		{
			String lastLocation = executor.getLastBukkitLocation();
					
			File file = new BukkitFinder().informedWalk(lastLocation);
			if (file != null)
			{
				staticId = stageId +2;
				executor.setDir(file);
			}
			
			else
			{
				file = new BukkitFinder().walk();
				if (file != null)
				{
					staticId = stageId +2;
					executor.setDir(file);
				}
				
				else
				{
					staticId = stageId+1;
				}
			}
				
		}
		if(this.getText() == TXT_PULL)
		{
			executor.execute("git pull");
			staticId = stageId+1;
		}
		if(this.getText() == TXT_GET_CHOICES)
		{
			executor.execute("git branch -r", true);
			staticId = stageId+1;
		}
		if(this.getText() == TXT_CHECKOUT)
		{
			executor.execute("git checkout " + this.executor.getGitFrame().getComboChoice(), true);
			staticId = stageId+1;
		}
		tellOthersToDecide();
	}
	private void tellOthersToDecide() 
	{
		for(int i = 0; i < list.size(); i++)
		{
			list.get(i).repaint();
		}
	}

	public void repaint()
	{
		decideIfEnabled();
		super.repaint();
	}

	private void decideIfEnabled() {
		if(staticId != stageId)
			this.setEnabled(false);
		else
			this.setEnabled(true);
	}

}
