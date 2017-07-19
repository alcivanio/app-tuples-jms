import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import classes.Generals;
import jms.ReceiverQueue;
import jms.SenderQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import models.Channel;
import models.ChannelCounter;
import models.Dispositive;
import models.DispositiveChannel;
import models.DispositiveCounter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MainUI extends JFrame {

	private JPanel contentPane;
	private Generals generals;
	JComboBox channelCombo;
	JComboBox deviceCombo;
	JButton createChannelBt;
	JButton deleteChannel;
	JButton createDeviceBt;
	JButton moveDevice;
	JButton deleteDevice;
	JComboBox toChannelCombo;
	JButton moveDone;
	JLabel label;
	JLabel lblAes;
	JLabel toSwitchLb;
	JButton btnMensagens;
	
	//aux variables
	ArrayList<Channel> channels;
	ArrayList<Dispositive> dispositives;
	
	Semaphore semUpdates = new Semaphore(1);
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI frame = new MainUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void initValues() {
		generals = new Generals();
		//clearAllSystem();
		setChannelsSelectVisible(false);
		//checkForNewChannels();
	}
	
	
	private void clearAllSystem() {
		Channel.clear(generals.space);//clear all the channels
		Dispositive.clear(generals.space);
		ChannelCounter.clear(generals.space);//clear all the channel counters
		DispositiveChannel.clear(generals.space);//clear all dispositive-channels
		DispositiveCounter.clear(generals.space);//clear all dispositive counters
	}
	
	
	private void setChannelsSelectVisible(boolean isVisible) {
		toChannelCombo.setVisible(isVisible);
		toSwitchLb.setVisible(isVisible);
		moveDone.setVisible(isVisible);
	}
	
	
	private void startActions() {
		createChannelBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { createChannelAction(); }
		});
		
		deleteChannel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { deleteChannelAction(); }
		});
		
		createDeviceBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { createDispositiveAction(); }
		});
		
		channelCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reloadDispositivesList();
			}
		});
		
		deleteDevice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				deleteDispositiveAction();
			}
		});
		
		moveDevice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDispositiveAction();
			}
		});
		
		moveDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doneMoveDispositiveAction();
			}
		});
		
		btnMensagens.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				messagesListAction();
			}
		});
		
		
	}
	
	
	

	/**
	 * Create the frame.
	 */
	public MainUI() {
		setTitle("PPD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 414, 344);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel nextChannelLb = new JLabel("Ambientes");
		nextChannelLb.setBounds(6, 6, 159, 16);
		contentPane.add(nextChannelLb);
		
		createChannelBt = new JButton("+canal");
		createChannelBt.setBounds(6, 29, 117, 29);
		contentPane.add(createChannelBt);
		
		JLabel nextDeviceLb = new JLabel("Canais");
		nextDeviceLb.setBounds(200, 6, 135, 16);
		contentPane.add(nextDeviceLb);
		
		createDeviceBt = new JButton("+dispositivo");
		createDeviceBt.setBounds(200, 29, 117, 29);
		contentPane.add(createDeviceBt);
		
		channelCombo = new JComboBox();
		channelCombo.setBounds(6, 126, 141, 27);
		contentPane.add(channelCombo);
		
		JLabel lblListaDeCanais = new JLabel("Lista de canais");
		lblListaDeCanais.setBounds(6, 98, 117, 16);
		contentPane.add(lblListaDeCanais);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 74, 135, 12);
		contentPane.add(separator);
		
		lblAes = new JLabel("Ações");
		lblAes.setBounds(6, 168, 117, 16);
		contentPane.add(lblAes);
		
		deleteChannel = new JButton("Deletar");
		deleteChannel.setBounds(6, 198, 71, 29);
		contentPane.add(deleteChannel);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(159, 18, 18, 271);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(182, 74, 135, 12);
		contentPane.add(separator_2);
		
		JLabel lblListaDeDispositivos = new JLabel("Lista de dispositivos");
		lblListaDeDispositivos.setBounds(182, 98, 135, 16);
		contentPane.add(lblListaDeDispositivos);
		
		toSwitchLb = new JLabel("Para");
		toSwitchLb.setBounds(188, 240, 61, 16);
		contentPane.add(toSwitchLb);
		
		deviceCombo = new JComboBox();
		deviceCombo.setBounds(178, 126, 135, 27);
		contentPane.add(deviceCombo);
		
		deleteDevice = new JButton("Deletar");
		deleteDevice.setBounds(178, 198, 80, 29);
		contentPane.add(deleteDevice);
		
		moveDevice = new JButton("Mover");
		moveDevice.setBounds(255, 198, 71, 29);
		contentPane.add(moveDevice);
		
		label = new JLabel("Ações");
		label.setBounds(182, 168, 61, 16);
		contentPane.add(label);
		
		toChannelCombo = new JComboBox();
		toChannelCombo.setBounds(182, 256, 135, 27);
		contentPane.add(toChannelCombo);
		
		moveDone = new JButton("Pronto");
		moveDone.setBounds(315, 255, 71, 29);
		contentPane.add(moveDone);
		
		btnMensagens = new JButton("Mensagens");
		btnMensagens.setBounds(6, 227, 117, 29);
		contentPane.add(btnMensagens);
		
		initValues();
		startActions();
	}
	
	
	
	//UI methods
	public void reloadChannelsList() {
		channels = Channel.getAll(generals.space);
		List<String> channelsName = new ArrayList<String>();
		for(Channel c : channels) {
			channelsName.add(c.name);
		}
		
		channelCombo.setModel(new DefaultComboBoxModel(channelsName.toArray()));	
	}
	
	
	public void acquire() {
		try {
			semUpdates.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void release() {
		semUpdates.release();
	}
	
	
	public void reloadDispositivesList() {
		if (channels == null || channels.size() == 0) { return; }
		
		int selectedIndex = channelCombo.getSelectedIndex();
		Channel sChannel = channels.get(selectedIndex);
		dispositives = Dispositive.getAll(sChannel, generals.space);
		
		List<String> dispositivesNames = new ArrayList<String>();
		for(Dispositive ds : dispositives) {
			dispositivesNames.add(ds.name);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				deviceCombo.setModel(new DefaultComboBoxModel(dispositivesNames.toArray()));
			}
		});
		
			
	}
	
	
	public void reloadToChannelList() {
		List<String> channelsName = new ArrayList<String>();
		for(Channel c : channels) {
			channelsName.add(c.name);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				toChannelCombo.setModel(new DefaultComboBoxModel(channelsName.toArray()));
			}
		});
		
		
	}
	
	
	
	
	public void checkForNewChannels() {
		Thread checkingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try { Thread.sleep(1500); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					checkNewChannelsAndDevices();
				}
			}
		});
		
		checkingThread.start();
	}
	
	
	public void checkNewChannelsAndDevices() {
		
	
		acquire();
		System.out.println("0");
		ArrayList<Channel> cs = Channel.getAll(generals.space);
		if(channels == null || channels.size() == 0) { 
			channels = cs;
			reloadChannelsList();
			reloadDispositivesList();
			System.out.println("1");
		}
		
		else {
			
			System.out.println("2");
			boolean cDifferent = false;
			for(int i=0; i<cs.size(); i++) {
				boolean found = false;
				for(int j=0; j<channels.size(); j++) {
					if(cs.get(i).id == channels.get(j).id) {found = true;}
				}
				if (!found) {cDifferent = true; i=1000;}
			}
			
			if (cDifferent) {
				reloadChannelsList();
				reloadDispositivesList();
				System.out.println("3");
			}
			
			if (channels != null && channels.size() > 0) {
				Channel c = channels.get(channelCombo.getSelectedIndex());
				ArrayList<Dispositive> ds = Dispositive.getAll(c, generals.space);
				System.out.println("4");
				if(dispositives == null || dispositives.size() == 0) {
					dispositives = ds;
					System.out.println("5");
					reloadDispositivesList();
				}
				
				else {
					System.out.println("6");
					cDifferent = false;
					for(int i=0; i<ds.size(); i++) {
						boolean found = false;
						for(int j=0; j<dispositives.size(); j++) {
							if (ds.get(i).id.intValue() == ds.get(j).id.intValue()) {found = true;}
						}
						if (!found) { cDifferent = true; i=1000; }
					}
					
					if (cDifferent) {
						reloadDispositivesList();
						System.out.println("7");
					}
				}
			}
			
		}
		
		
		
		release();
		System.out.println("8");
	}
	
	
	
	//actions
	public void createChannelAction() {
		acquire();
		ChannelCounter cCounter = ChannelCounter.get(generals.space);
		String cName 			= "amb" + (cCounter.count + 1); 
		Channel c 				= new Channel(cCounter.count, cName); 
		
		Channel.add(c, generals.space, cCounter);
		reloadChannelsList();
		release();
	}
	
	
	public void deleteChannelAction() {
		if (channels == null || channels.size() == 0) { return; }
		
		acquire();
		int selectedIndex = channelCombo.getSelectedIndex();
		Channel cToDelete = channels.get(selectedIndex);
		Channel.remove(cToDelete, generals.space);
		reloadChannelsList();
		release();
	}
	
	
	public void createDispositiveAction() {
		if (channels == null || channels.size() == 0) { return; }
		
		acquire();
		int selectedChannelIndex = channelCombo.getSelectedIndex();
		Channel sChannel = channels.get(selectedChannelIndex);
		DispositiveCounter dCounter = DispositiveCounter.get(generals.space);
		String dcName = "disp" + (dCounter.count + 1);
		Dispositive dsp = new Dispositive(dCounter.count, dcName);
		
		Dispositive.add(dsp, sChannel, generals.space, dCounter);
		reloadDispositivesList();
		release();
		
		/*ChannelCounter cCounter = ChannelCounter.get(generals.space);
		String cName 			= "dev" + cCounter.count; 
		Channel c 				= new Channel(cCounter.count, cName); 
		
		Channel.add(c, generals.space, cCounter);
		reloadChannelsList();*/
	}
	
	
	public void deleteDispositiveAction() {
		if (dispositives == null || dispositives.size() == 0) { return; }
		
		acquire();
		int selectedIndex = deviceCombo.getSelectedIndex();
		Dispositive d = dispositives.get(selectedIndex);
		
		Dispositive.remove(d, generals.space);
		reloadDispositivesList();
		release();
	}
	
	
	public void moveDispositiveAction() {
		if (dispositives == null || dispositives.size() == 0) { return; }
		
		acquire();
		reloadToChannelList();
		setChannelsSelectVisible(true);
		release();
	}
	
	
	public void doneMoveDispositiveAction() {
		if (dispositives == null || dispositives.size() == 0 || channels == null || channels.size() == 0) { return; }
		
		acquire();
		int sIndexFromChannel	= channelCombo.getSelectedIndex();
		int sIndexToChannel 		= toChannelCombo.getSelectedIndex();
		int sIndexDevices 		= deviceCombo.getSelectedIndex();
		
		Dispositive d 	= dispositives.get(sIndexDevices);
		Channel fromC	= channels.get(sIndexFromChannel);
		Channel toC 		= channels.get(sIndexToChannel);
		
		Dispositive.move(d, fromC, toC, generals.space);
		
		setChannelsSelectVisible(false);
		reloadDispositivesList();
		
		release();
	}
	
	
	public void messagesListAction() {
		if (channels == null || channels.size()  == 0) { return; }
		
		int sIndexChannel = channelCombo.getSelectedIndex();
		Channel c = channels.get(sIndexChannel);
		
		ArrayList<String> newMessages = ReceiverQueue.readMessages(c);
		Channel.addMessage(c, newMessages, generals.space);
		
		String fMessage = "";
		for (String s : newMessages) {
			fMessage = fMessage + "\n";
			fMessage = fMessage + s;
		}
		
		JOptionPane.showMessageDialog(this, fMessage);
		
	}
	
	
	
	
}










