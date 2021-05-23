import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dad.entity.SunPosition;

public class pruebaCSV {

	public static final String SEPARADOR = ";";

	public static void main(String[] args) {

		BufferedReader bufferLectura = null;
		String[] camposCabecera;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		List<List<SunPosition>> lista = new ArrayList<List<SunPosition>>();
		try {
			// Abrir el .csv en buffer de lectura
			bufferLectura = new BufferedReader(new FileReader("archivo.csv"));

			// Leer una linea del archivo
			String cabecera = bufferLectura.readLine();

			camposCabecera = cabecera.split(SEPARADOR);

			String linea = bufferLectura.readLine();
			// linea = bufferLectura.readLine();
			while (linea != null) {
				List<SunPosition> lista2 = new ArrayList<SunPosition>();
				// Sepapar la linea leída con el separador definido previamente
				String[] campos = linea.split(SEPARADOR);

				//System.out.println(Arrays.toString(campos));
				for (int i = 1; i < campos.length; i += 2) {
					lista2.add(
							new SunPosition(1, 1,
									LocalDateTime.parse(campos[0] + " " + camposCabecera[i].replace("E", "").trim(),
											formatter),
									Float.valueOf(campos[i].equals("--") ? "0" : campos[i]),
									Float.valueOf(campos[i + 1].equals("--") ? "0" : campos[i + 1])));
					}

				// Volver a leer otra línea del fichero
				lista.add(lista2);
				linea = bufferLectura.readLine();
				
			}
			for(int i = 0; i < lista.size(); i++) {
				System.out.println(lista.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Cierro el buffer de lectura
			if (bufferLectura != null) {
				try {
					bufferLectura.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
