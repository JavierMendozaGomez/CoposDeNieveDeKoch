package model;

import controller.Lista;

public class Copo {
	Lista lista;
	Lapiz lapiz;

	public Copo(){
		this.lista = new Lista();
	}

	public Lista inicializarTriangulo(Lapiz lapiz, double xTriangle, double yTriangle, double triangleSide) {

		this.lista = new Lista();
		lapiz = new Lapiz(); //Instanciamos la clase lapiz vacia (origend e coordenadas y dir = 0)
		Segmento segmento; 
		PV2D pos = new PV2D(); //Creamos un punto (x,y)
		pos.setX(xTriangle); //Modificamos la x e y del punto a los valores iniciales calculados al instanciar la clase scene
		pos.setY(yTriangle);

		lapiz.moveTo(pos, false); //Movemos el lapiz a la pos inicial 

		lapiz.turn(0); //Giramos 0 grados

		segmento = new Segmento(pos.getX(), pos.getY(), lapiz.getDirActual(), triangleSide); //Inicializamos el segmento inical (el paralelo al eje x)
		lista.addSegment(segmento); //Añadimos el segmento a la lista de segmentos

		lapiz.forward(triangleSide, true); //Movemos el lapiz la distancia triangleSide con direccion 0 grados
		pos = lapiz.getPosActual(); //Obtenemos la posición actual del lapiz

		lapiz.turn(120);

		segmento = new Segmento(pos.getX(), pos.getY(), lapiz.getDirActual(), triangleSide);
		lista.addSegment(segmento);

		lapiz.forward(triangleSide, true);
		pos = lapiz.getPosActual();

		lapiz.turn(120);

		segmento = new Segmento(pos.getX(), pos.getY(), lapiz.getDirActual(), triangleSide);
		lista.addSegment(segmento);		
		return lista; 
	}

	public Lista anidar(Lista listaIn) {

		lapiz = new Lapiz();
		this.lista = new Lista(); //Inicializamos a 0 la lista de la clase copo

		int numSegmentos = listaIn.getSize(); //Obtenemos el tamaño de la lista qu enos pasan como parametro
		Segmento segmento;

		PV2D pos;
		lapiz.moveTo(listaIn.getSegmento(0).getInitPoint(), false); //Nos movemos a la posición del punto inical

		for(int i = 0; i < numSegmentos; i++){			
			segmento = listaIn.getSegmento(i);	//Obtenemos un segmento inicial, que dividiremos en 4 subsegmentos
			pos = segmento.getInitPoint(); //Obtenemos el punto inicial del segmento			
			lista.addSegment(new Segmento(pos.getX(), pos.getY(), segmento.getDir(), segmento.getDist()/3)); //Guardamos el primer tercio del segmento original, que sera un nuevo segmento

			lapiz.turnTo(segmento.getDir()); //Cambiamos la direccion del lapiz a la que tiene el segmento (DirLapiz - DirLapiz + segmentoDir)

			lapiz.forward(segmento.getDist()/3, false); //Nos desplazamos un tercio del tamaño del segmento
			pos = lapiz.getPosActual(); //Obtenemos los puntos de la nueva posicion


			lapiz.turn(-60.0);

			lista.addSegment(new Segmento(pos.getX(), pos.getY(), lapiz.getDirActual(), segmento.getDist()/3)); //Guardamos el nuevo segmento con la direccion que tiene el lapiz
			lapiz.forward(segmento.getDist()/3, false);
			pos = lapiz.getPosActual();

			lapiz.turn(120.0);

			lista.addSegment(new Segmento(pos.getX(), pos.getY(),  lapiz.getDirActual(), segmento.getDist()/3));		
			lapiz.forward(segmento.getDist()/3, false);
			pos = lapiz.getPosActual();

			lapiz.turn(-60.0);
			lista.addSegment(new Segmento(pos.getX(), pos.getY(),  lapiz.getDirActual(), segmento.getDist()/3));	
			lapiz.forward(segmento.getDist()/3, false);
		}		
		return this.lista;
	}

	public Lista desanidar(Lista listaIn) {
		lapiz = new Lapiz(); 	

		int numSegmentos = listaIn.getSize(); //Obtenemos el tamaño de la lista qu enos pasan como parametro
		Segmento segmento;
		PV2D pos;
		
		lapiz.moveTo(listaIn.getSegmento(0).getInitPoint(), false); //Movemos el lapiz a la posición del punto inical
		if(numSegmentos > 3){ //Si el numero de segmentos es mayor que 3 usamos el patron
			this.lista = new Lista(); //Inicializamos a 0 la lista de la clase copo
			
			for(int i = 0; i < numSegmentos; i++){
				segmento = listaIn.getSegmento(i);	//Obtenemos un segmento inicial, que dividiremos en 4 subsegmentos
				pos = segmento.getInitPoint(); //Obtenemos el punto inicial del segmento			
				lista.addSegment(new Segmento(pos.getX(), pos.getY(), segmento.getDir(), segmento.getDist()*3));		
				i+=3;
			}
		}
		return this.lista;
	}


}
