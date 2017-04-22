/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.algoritmos;

import java.awt.BorderLayout;
import javafx.scene.control.TreeItem;
import javax.swing.JPanel;

/**
 *
 * @author Daniel
 */
public class PruebaGUI extends javax.swing.JFrame {

    /**
     * Creates new form PruebaGUI
     */
    
    public JPanel panelSeleccionado;
    public Mercancia panelDeMercancia;
    public static WeightedGraphInterface<Truck, Service, Merchandise> graph = new WeightedGraph<Truck, Service, Merchandise>();
    private static Truck[] trucks = new Truck[50];
    private static Merchandise[] mercancias = new Merchandise[50];
    private static int numCam=0, numMer=0;
    private BuscarRuta ruta;
    
    	private static void shortestPaths(WeightedGraphInterface<Truck, Service, Merchandise> graph,
			Truck startVertex  )
	{
		Route flight;
		Route saveFlight;        
		int minDistance;
		int newDistance;

		PriQueueInterface<Route> pq = new Heap<Route>(20);  
		Truck vertex;
		UnboundedQueueInterface<Truck, Service, Merchandise> vertexQueue = new LinkedUnbndQueue<Truck, Service, Merchandise>();

		graph.clearMarks();
		saveFlight = new Route(startVertex, startVertex, 0);
		pq.enqueue(saveFlight);

		do
		{
			flight = pq.dequeue();
			if (!graph.isMarked(flight.getToVertex()))
			{
				graph.markVertex(flight.getToVertex());
				System.out.println(flight);
				flight.setFromVertex(flight.getToVertex());
				minDistance = flight.getDistance();
				vertexQueue = graph.getToVertices(flight.getFromVertex());
				while (!vertexQueue.isEmpty())
				{
					/*vertex = vertexQueue.dequeue();
					if (!graph.isMarked(vertex))
					{
						newDistance = minDistance 
								+ graph.weightIs(flight.getFromVertex(), vertex);
						saveFlight = new Route(flight.getFromVertex(), vertex, newDistance);
						pq.enqueue(saveFlight);
					}*/
				}
			}
		} while (!pq.isEmpty());
		System.out.println();
		System.out.println("The unreachable vertices are:");
		vertex = graph.getUnmarked();
		while (vertex != null)
		{
			System.out.println(vertex);
			graph.markVertex(vertex);
			vertex = graph.getUnmarked();
		}
		System.out.println();
	}
	
	private static <T> boolean isPath(WeightedGraphInterface<Truck, Service, Merchandise> graph,
			Truck startVertex, 
			Merchandise endVertex)

	{
		UnboundedStackInterface<Truck> stack = new LinkedStack<Truck>();
		UnboundedQueueInterface<Truck, Service, Merchandise> vertexQueue = 
				new LinkedUnbndQueue<Truck, Service, Merchandise>();

		boolean found = false;
		Truck vertex;
		T item;

		graph.clearMarks();
		stack.push(startVertex);
		do
		{
			vertex = stack.top();
			stack.pop();
			if (vertex.getNumMer() == endVertex.getNumber())
				found = true;
			else
			{
				if (!graph.isMarked(vertex))
				{
					graph.markVertex(vertex);
					vertexQueue = graph.getToVertices(vertex);

					while (!vertexQueue.isEmpty())
					{
						item = (T) vertexQueue.dequeue();
					}
				}
			}
		} while (!stack.isEmpty() && !found);

		return found;
	}
	
	
	private static <T> boolean isPath2(WeightedGraphInterface<Truck, Service, Merchandise> graph,
			Truck startVertex, 
			Merchandise endVertex    )

	{
		UnboundedQueueInterface<Truck, Service, Merchandise> queue = new LinkedUnbndQueue<Truck, Service, Merchandise>();
		UnboundedQueueInterface<Truck, Service, Merchandise> vertexQueue = new LinkedUnbndQueue<Truck, Service, Merchandise>();

		boolean found = false;
		Truck vertex;
		T item;

		graph.clearMarks();
		queue.enqueue(startVertex);
		do
		{
			vertex = queue.dequeue();
			if (vertex.getNumMer() == endVertex.getNumber())
				found = true;
			else
			{
				if (!graph.isMarked(vertex))
				{
					graph.markVertex(vertex);
					vertexQueue = graph.getToVertices(vertex);

					while (!vertexQueue.isEmpty())
					{
						item = (T) vertexQueue.dequeue();
					}
				}
			}
		} while (!queue.isEmpty() && !found);

		return found;
	}
    
    public PruebaGUI() {
        initComponents();
    }
    
    public void agregarMercancia(Merchandise mer){
        this.graph.addVertex3(mer);
        this.mercancias[numMer]= mer;
        numMer ++;
        setText(this.graph.toString());
    }
    
    public void agregarCamion(Truck t){
        this.graph.addVertex(t);
        trucks[numCam] = t;
        numCam++;
        setText(this.graph.toString());
    }
    
    public void agregarServicio(Service ser, int price, String chofer,String merca){
       this.graph.addVertex2(ser);
       for (int i=0; i<trucks.length; i++){
           if (trucks[i]!=null){
                if (trucks[i].getName().equals(chofer)){
                this.graph.addEdge(trucks[i], ser, price);
                } 
           }

       }
       
       for (int i=0; i<mercancias.length; i++){
           if (mercancias[i]!=null){
                if (mercancias[i].getName().equals(merca)){
                this.graph.addEdge3(ser, mercancias[i], 0);
                } 
           }

       }
       setText(this.graph.toString());
       jLabel1.setText(this.graph.sortPrices()+ "");
               
    }
    
    public void searchMer(String chofe,String mer){
        Truck camioncito = null;
        Merchandise merca =  null; 
        for (int i=0; i<trucks.length; i++){
           if (trucks[i]!=null){
                if (trucks[i].getName().equals(chofe)){
                camioncito= trucks[i];
                } 
           }

       }
       
       for (int i=0; i<mercancias.length; i++){
           if (mercancias[i]!=null){
                if (mercancias[i].getName().equals(mer)){
                    merca = mercancias[i];
                } 
           }

       }
        camioncito.setNumMer(merca.getNumber());
        boolean result = isPath(graph, camioncito, merca);
        ruta.setLabel(result);
        
    }
    
    public void setPanelRuta(BuscarRuta ruta){
        this.ruta = ruta;
    }
    
    public void changePanel(String panelName){
        
        if(panelName == "Merchandises"){
            this.panelDeMercancia = new Mercancia(this);
            panelDeMercancia.setSize(680, 120);
            panelDeMercancia.setLocation(5, 5);
            
            jPrincipalPanel.removeAll();
            jPrincipalPanel.add(panelDeMercancia,BorderLayout.CENTER);
            jPrincipalPanel.revalidate();
            jPrincipalPanel.repaint();
            
        }
        
        if(panelName == "Services"){
            panelSeleccionado = new Servicios(this);
            panelSeleccionado.setSize(680, 120);
            panelSeleccionado.setLocation(5, 5);
            
            jPrincipalPanel.removeAll();
            jPrincipalPanel.add(panelSeleccionado,BorderLayout.CENTER);
            jPrincipalPanel.revalidate();
            jPrincipalPanel.repaint();
        }
        
        if(panelName == "Trucks"){
            panelSeleccionado= new Camiones(this);
            panelSeleccionado.setSize(680, 120);
            panelSeleccionado.setLocation(5, 5);
            
            jPrincipalPanel.removeAll();
            jPrincipalPanel.add(panelSeleccionado,BorderLayout.CENTER);
            jPrincipalPanel.revalidate();
            jPrincipalPanel.repaint();
        }
        
        if(panelName == "Search of Mer."){
            panelSeleccionado= new BuscarRuta(this);
            panelSeleccionado.setSize(680, 120);
            panelSeleccionado.setLocation(5, 5);
            
            jPrincipalPanel.removeAll();
            jPrincipalPanel.add(panelSeleccionado,BorderLayout.CENTER);
            jPrincipalPanel.revalidate();
            jPrincipalPanel.repaint();
        }
    }
    
    public int getNumSer(){
        return this.graph.getNumVertices();
    }
    
    public void setText(String texto){
        this.jAreaDeImpresion.setText(texto);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPrincipalPanel = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        SelectButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jAreaDeImpresion = new javax.swing.JTextArea();
        Titulo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        BiggestPrice = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPrincipalPanelLayout = new javax.swing.GroupLayout(jPrincipalPanel);
        jPrincipalPanel.setLayout(jPrincipalPanelLayout);
        jPrincipalPanelLayout.setHorizontalGroup(
            jPrincipalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        jPrincipalPanelLayout.setVerticalGroup(
            jPrincipalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Services", "Trucks", "Merchandises", "Search of Mer.", " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        SelectButton.setText("Select");
        SelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectButtonActionPerformed(evt);
            }
        });

        jAreaDeImpresion.setColumns(20);
        jAreaDeImpresion.setRows(5);
        jScrollPane2.setViewportView(jAreaDeImpresion);

        Titulo.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        Titulo.setText("Truck Company System");

        BiggestPrice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        BiggestPrice.setText("Biggest Price: ");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("_");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1)
                            .addComponent(jScrollPane2)
                            .addComponent(jPrincipalPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SelectButton)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(Titulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BiggestPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SelectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPrincipalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BiggestPrice)
                    .addComponent(jLabel1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectButtonActionPerformed
        changePanel(this.jComboBox1.getSelectedItem().toString());
        
        
    }//GEN-LAST:event_SelectButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PruebaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PruebaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PruebaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PruebaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        
		
		Truck t = new Truck("Luis Garcia", 99543, 4567);
		
                trucks[numCam] = t;
                
                numCam++;
		
		Truck t2 = new Truck("Ramon Corona", 1012, 4599);
		
                trucks[numCam] = t2;
                
                numCam++;
		
		Service s = new Service ("Luisa Inc.", "Acapulco", 1);
		
		
		Service s2 = new Service ("Ramona Company", "Zapopan", 2);
		
		
		Service s3 = new Service ("Bonafont", "Toluca", 3);
		
		
		Merchandise m1 = new Merchandise ("Peaches", 10);
                
                mercancias[numMer] = m1;
                
                numMer++;
		
		Merchandise m2 = new Merchandise ("CD's", 20);
                
                mercancias[numMer] = m2;
                
                numMer++;
		
		graph.addVertex(t);
		
		graph.addVertex(t2);
		
		graph.addVertex2(s);
		
		graph.addVertex2(s2);
		
		graph.addVertex2(s3);
		
		graph.addEdge(t, s, 800);
		
		graph.addEdge(t2, s2, 1249);
		
		graph.addVertex3(m1);
		
		graph.addVertex3(m2);
		
		graph.addEdge3(s, m1, 0);
		
		graph.addEdge3(s2, m2, 0);
		
		Truck t3 = new Truck ("Sergio Lopez", 37373, 01010);
                
                trucks[numCam] = t3;
                
                numCam++;
		
		Service s4 = new Service ("Coca cola", "Chilpancingo", 4);
		
		Merchandise m3 = new Merchandise ("Candy", 203);
                
                mercancias[numMer] = m3;
                
                numMer++;
		
		graph.addVertex(t3);
		
		graph.addVertex2(s4);
		
		graph.addVertex3(m3);
		
		graph.addEdge(t3, s4, 250);
		
		graph.addEdge3(s4, m3, 0);
		
		t.setNumMer(m1.getNumber());
		
		t2.setNumMer(m2.getNumber());
		
		t3.setNumMer(m3.getNumber());
		
		Truck t5 = new Truck ("Dolores Cordero", 939293, 929293);
                
                trucks[numCam] = t5;
                
                numCam++;
		
		Service s5 = new Service ("Sabritas", "Chiapas", 5);
		
		graph.addVertex(t5);
		
		graph.addVertex2(s5);
		
		graph.addEdge(t5, s5, 2);
                
                
                
                
                
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PruebaGUI prueba = new PruebaGUI();
                prueba.setVisible(true);
                prueba.setText(graph.toString());
                prueba.jLabel1.setText(graph.sortPrices() + "");
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BiggestPrice;
    private javax.swing.JButton SelectButton;
    private javax.swing.JLabel Titulo;
    private javax.swing.JTextArea jAreaDeImpresion;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JPanel jPrincipalPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
