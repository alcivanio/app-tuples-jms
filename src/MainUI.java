import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import classes.Generals;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
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

public class MainUI extends JFrame {

	private JPanel contentPane;
	private Generals generals;
	JComboBox channelCombo;
	JComboBox deviceCombo;
	JButton createChannelBt;
	JButton deleteChannel;
	JButton createDeviceBt;
	
	
	
	//aux variables
	ArrayList<Channel> channels;
	ArrayList<Dispositive> dispositives;
	

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
		clearAllSystem();
	}
	
	
	private void clearAllSystem() {
		Channel.clear(generals.space);//clear all the channels
		Dispositive.clear(generals.space);
		ChannelCounter.clear(generals.space);//clear all the channel counters
		DispositiveChannel.clear(generals.space);//clear all dispositive-channels
		DispositiveCounter.clear(generals.space);//clear all dispositive counters
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
		
		JLabel nextChannelLb = new JLabel("prox. canal: amb1");
		nextChannelLb.setBounds(6, 6, 159, 16);
		contentPane.add(nextChannelLb);
		
		createChannelBt = new JButton("+canal");
		createChannelBt.setBounds(6, 29, 117, 29);
		contentPane.add(createChannelBt);
		
		JLabel nextDeviceLb = new JLabel("prox. disp: disp1");
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
		
		JLabel lblAes = new JLabel("Ações");
		lblAes.setBounds(6, 168, 117, 16);
		contentPane.add(lblAes);
		
		deleteChannel = new JButton("Deletar");
		deleteChannel.setBounds(6, 198, 71, 29);
		contentPane.add(deleteChannel);
		
		JButton listChannel = new JButton("Listar");
		listChannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		listChannel.setBounds(75, 198, 80, 29);
		contentPane.add(listChannel);
		
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
		
		JLabel toSwitchLb = new JLabel("Para");
		toSwitchLb.setBounds(188, 240, 61, 16);
		contentPane.add(toSwitchLb);
		
		deviceCombo = new JComboBox();
		deviceCombo.setBounds(178, 126, 135, 27);
		contentPane.add(deviceCombo);
		
		JButton deleteDevice = new JButton("Deletar");
		deleteDevice.setBounds(178, 198, 80, 29);
		contentPane.add(deleteDevice);
		
		JButton moveDevice = new JButton("Mover");
		moveDevice.setBounds(255, 198, 71, 29);
		contentPane.add(moveDevice);
		
		JLabel label = new JLabel("Ações");
		label.setBounds(182, 168, 61, 16);
		contentPane.add(label);
		
		JComboBox toChannelCombo = new JComboBox();
		toChannelCombo.setBounds(182, 256, 135, 27);
		contentPane.add(toChannelCombo);
		
		JButton moveDone = new JButton("Pronto");
		moveDone.setBounds(315, 255, 71, 29);
		contentPane.add(moveDone);
		
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
	
	
	public void reloadDispositivesList() {
		if (channels == null || channels.size() == 0) { return; }
		
		int selectedIndex = channelCombo.getSelectedIndex();
		Channel sChannel = channels.get(selectedIndex);
		dispositives = Dispositive.getAll(sChannel, generals.space);
		
		List<String> dispositivesNames = new ArrayList<String>();
		for(Dispositive ds : dispositives) {
			dispositivesNames.add(ds.name);
		}
		
		deviceCombo.setModel(new DefaultComboBoxModel(dispositivesNames.toArray()));	
	}
	
	
	
	
	
	
	//actions
	public void createChannelAction() {
		ChannelCounter cCounter = ChannelCounter.get(generals.space);
		String cName 			= "amb" + (cCounter.count + 1); 
		Channel c 				= new Channel(cCounter.count, cName); 
		
		Channel.add(c, generals.space, cCounter);
		reloadChannelsList();
	}
	
	
	public void deleteChannelAction() {
		if (channels == null || channels.size() == 0) { return; }
		
		int selectedIndex = channelCombo.getSelectedIndex();
		Channel cToDelete = channels.get(selectedIndex);
		Channel.remove(cToDelete, generals.space);
		reloadChannelsList();
	}
	
	
	public void createDispositiveAction() {
		if (channels == null || channels.size() == 0) { return; }
		int selectedChannelIndex = channelCombo.getSelectedIndex();
		Channel sChannel = channels.get(selectedChannelIndex);
		
		DispositiveCounter dCounter = DispositiveCounter.get(generals.space);
		String dcName = "disp" + (dCounter.count + 1);
		Dispositive dsp = new Dispositive(dCounter.count, dcName);
		
		Dispositive.add(dsp, sChannel, generals.space, dCounter);
		reloadDispositivesList();
		
		/*ChannelCounter cCounter = ChannelCounter.get(generals.space);
		String cName 			= "dev" + cCounter.count; 
		Channel c 				= new Channel(cCounter.count, cName); 
		
		Channel.add(c, generals.space, cCounter);
		reloadChannelsList();*/
	}
	
	
	
	
	
	
	
	
}
