/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.monitoreoenergetico;

import java.util.Scanner;
import java.util.Random;

public class MonitoreoEnergetico {

    static final int NUM_EQUIPOS = 5;
    static final int NUM_PERIODOS = 5;
    static final double UMBRAL_DIA = 50;
    static final double FACTOR_ALTO = 1.2;
    static final double FACTOR_CRITICO = 1.5;
    static final double FACTOR_BAJO = 0.7;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        String[] nombresEquipos = new String[6]; 
        String[] diasNombres = new String[6];
        String[] idBinario = new String[6];
        String[] estado = new String[6];
        double[][] consumo = new double[6][6];
        double[] totalEquipo = new double[6];
        double[] porcentaje = new double[6];
        String[] clasificacion = new String[6];
        String[] tendencia = new String[6];
        boolean[] alerta = new boolean[6];
        String[] recomendacion = new String[6];
        int[] orden = new int[6];

        int i, j;
        double totalGeneral;
        double promedioGeneral = 0;
        double maxConsumo = 0;
        double minConsumo = 0;
        int posMax = 1, posMin = 1;
        int contadorAlertas;
        int equiposEncendidos;
        int opcion;
        int k;
        int valorEstado;
        boolean datosRegistrados;
        boolean requiereRecalculo;

        nombresEquipos[1] = "Iluminacion";
        nombresEquipos[2] = "Aire acondicionado";
        nombresEquipos[3] = "Laboratorios";
        nombresEquipos[4] = "Oficinas";
        nombresEquipos[5] = "Equipos de computo";

        diasNombres[1] = "Lunes";
        diasNombres[2] = "Martes";
        diasNombres[3] = "Miercoles";
        diasNombres[4] = "Jueves";
        diasNombres[5] = "Viernes";

        for (i = 1; i <= NUM_EQUIPOS; i++) {
            idBinario[i] = convertirABinario(i);
            orden[i] = i;
        }

        totalGeneral = 0;
        contadorAlertas = 0;
        equiposEncendidos = 0;
        datosRegistrados = false;

        System.out.println("==============================================================");
        System.out.println("   SISTEMA DE MONITOREO Y CONTROL DE CONSUMO ENERGETICO");
        System.out.println("   Universidad Tecnica de Machala - Grupo 5");
        System.out.println("==============================================================");

        do {

            System.out.println();
            System.out.println("================== MENU PRINCIPAL ==========================");
            System.out.println(" 1. Registrar consumo semanal de todos los equipos");
            System.out.println(" 2. Actualizar el consumo de un equipo especifico");
            System.out.println(" 3. Ver historial de consumos por periodo");
            System.out.println(" 4. Ver estadistica descriptiva general");
            System.out.println(" 5. Ver procedimiento matematico (formulas)");
            System.out.println(" 6. Ver clasificacion automatica y tendencia");
            System.out.println(" 7. Ver ranking de equipos por consumo");
            System.out.println(" 8. Ver recomendaciones del sistema");
            System.out.println(" 9. Ver alertas generadas");
            System.out.println("10. Modo simulacion (generar datos aleatorios)");
            System.out.println(" 0. Salir");
            System.out.println("==============================================================");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt();

            requiereRecalculo = false;

            switch (opcion) {

                case 1:
                    System.out.println();
                    System.out.println("--- REGISTRO DE CONSUMOS POR EQUIPO/AREA ---");

                    for (i = 1; i <= NUM_EQUIPOS; i++) {
                        System.out.println();
                        System.out.println("Equipo/Area: " + nombresEquipos[i]);

                        System.out.print("  Ingrese el estado (0=Encendido, 1=Apagado): ");
                        valorEstado = sc.nextInt();
                        while (valorEstado != 0 && valorEstado != 1) {
                            System.out.print("  ERROR: escriba 0 para Encendido o 1 para Apagado: ");
                            valorEstado = sc.nextInt();
                        }
                        
                        estado[i] = (valorEstado == 0) ? "Encendido" : "Apagado";

                        if (estado[i].equals("Apagado")) {
                            System.out.println("  [Info] Equipo apagado: no consume energia. Se registra 0 kWh en los 5 dias.");
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                consumo[i][j] = 0;
                            }
                        } else {
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                System.out.print("  Consumo del " + diasNombres[j] + " (kWh): ");
                                consumo[i][j] = sc.nextDouble();

                                while (consumo[i][j] < 0) {
                                    System.out.print("  ERROR: el consumo no puede ser negativo. Reingrese: ");
                                    consumo[i][j] = sc.nextDouble();
                                }

                                if (consumo[i][j] > UMBRAL_DIA) {
                                    System.out.println("    [Aviso] Consumo del dia supera el umbral diario de " + UMBRAL_DIA + " kWh.");
                                }
                            }
                        }
                    }

                    datosRegistrados = true;
                    requiereRecalculo = true;
                    System.out.println();
                    System.out.println("[OK] Registro completo. Estadisticas recalculadas.");
                    break;

                case 2:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("Seleccione el equipo a actualizar:");
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            System.out.println("  " + i + ". " + nombresEquipos[i]);
                        }
                        k = sc.nextInt();
                        while (k < 1 || k > NUM_EQUIPOS) {
                            System.out.print("Opcion invalida. Ingrese un numero entre 1 y " + NUM_EQUIPOS + ": ");
                            k = sc.nextInt();
                        }

                        System.out.print("  Nuevo estado de " + nombresEquipos[k] + " (0=Encendido, 1=Apagado): ");
                        valorEstado = sc.nextInt();
                        while (valorEstado != 0 && valorEstado != 1) {
                            System.out.print("  ERROR: escriba 0 para Encendido o 1 para Apagado: ");
                            valorEstado = sc.nextInt();
                        }
                        estado[k] = (valorEstado == 0) ? "Encendido" : "Apagado";

                        if (estado[k].equals("Apagado")) {
                            System.out.println("  [Info] Equipo apagado: no consume energia. Se registra 0 kWh en los 5 dias.");
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                consumo[k][j] = 0;
                            }
                        } else {
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                System.out.print("  Nuevo consumo del " + diasNombres[j] + " (kWh): ");
                                consumo[k][j] = sc.nextDouble();
                                while (consumo[k][j] < 0) {
                                    System.out.print("  ERROR: el consumo no puede ser negativo. Reingrese: ");
                                    consumo[k][j] = sc.nextDouble();
                                }
                            }
                        }

                        System.out.println();
                        System.out.println("[OK] " + nombresEquipos[k] + " actualizado. Recalculando el sistema...");
                        requiereRecalculo = true;
                    }
                    break;

                case 3:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- HISTORIAL DE CONSUMOS POR PERIODO (kWh) ---");
                        System.out.println();
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            System.out.println("ID:" + idBinario[i] + " (bin)  Equipo: " + nombresEquipos[i] + "  Estado: " + estado[i]);
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                System.out.println("     " + diasNombres[j] + " : " + consumo[i][j] + " kWh");
                            }
                            System.out.println("     Total semanal del equipo: " + totalEquipo[i] + " kWh");
                            System.out.println();
                        }
                    }
                    break;

                case 4:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- ESTADISTICA DESCRIPTIVA GENERAL ---");
                        System.out.println("  Consumo total del sistema : " + totalGeneral + " kWh");
                        System.out.println("  Consumo promedio por equipo: " + promedioGeneral + " kWh");
                        System.out.println("  Consumo maximo : " + nombresEquipos[posMax] + " (" + maxConsumo + " kWh)");
                        System.out.println("  Consumo minimo : " + nombresEquipos[posMin] + " (" + minConsumo + " kWh)");
                    }
                    break;

                case 5:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- PROCEDIMIENTO MATEMATICO ---");
                        mostrarProcedimientoPromedio(totalGeneral, NUM_EQUIPOS, promedioGeneral);
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            System.out.println("  % de " + nombresEquipos[i] + " = (" + totalEquipo[i] + " / " + totalGeneral + ") * 100 = " + porcentaje[i] + "%");
                        }
                        mostrarConversionUnidades(totalGeneral);
                    }
                    break;

                case 6:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- CLASIFICACION AUTOMATICA Y TENDENCIA ---");
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            System.out.println("  " + nombresEquipos[i] + " -> Nivel: " + clasificacion[i] + " | Tendencia (Lunes->Viernes): " + tendencia[i]);
                        }
                    }
                    break;

                case 7:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- RANKING DE EQUIPOS POR CONSUMO (mayor a menor) ---");
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            System.out.println("  " + i + ". " + nombresEquipos[orden[i]] + " - " + totalEquipo[orden[i]] + " kWh (" + porcentaje[orden[i]] + "%)");
                        }
                    }
                    break;

                case 8:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- RECOMENDACIONES DEL SISTEMA ---");
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            System.out.println("  * " + recomendacion[i]);
                        }
                        if (equiposEncendidos > 1 && contadorAlertas > 1) {
                            System.out.println("  * Se recomienda redistribuir los horarios de funcionamiento, ya que existen " + contadorAlertas + " equipos con consumo elevado encendidos simultaneamente.");
                        }
                    }
                    break;

                case 9:
                    if (!datosRegistrados) {
                        System.out.println("Debe registrar los datos primero (opcion 1).");
                    } else {
                        System.out.println();
                        System.out.println("--- ALERTAS GENERADAS (regla: Consumo Alto/Critico Y Encendido) ---");
                        for (i = 1; i <= NUM_EQUIPOS; i++) {
                            if (alerta[i]) {
                                System.out.println("  [ALERTA] " + nombresEquipos[i] + ": nivel " + clasificacion[i] + " y esta Encendido -> se activa la alerta.");
                            }
                        }
                        if (contadorAlertas == 0) {
                            System.out.println("  No se generaron alertas. Ningun equipo encendido supera el nivel Alto/Critico.");
                        }
                    }
                    break;

                case 10:
                   
                    System.out.println();
                    System.out.println("--- MODO SIMULACION: generando datos aleatorios ---");

                    for (i = 1; i <= NUM_EQUIPOS; i++) {
                        // rnd.nextInt(2) devuelve 0 o 1 al azar.
                        valorEstado = rnd.nextInt(2);
                        estado[i] = (valorEstado == 0) ? "Encendido" : "Apagado";

                        if (estado[i].equals("Apagado")) {
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                consumo[i][j] = 0;
                            }
                        } else {
                            for (j = 1; j <= NUM_PERIODOS; j++) {
                                // rnd.nextInt(71) da un numero entero entre 0 y 70,
                                // para que a veces se generen consumos altos y
                                // se disparen alertas de prueba.
                                consumo[i][j] = rnd.nextInt(71);
                            }
                        }
                        System.out.println("  " + nombresEquipos[i] + " -> Estado: " + estado[i]);
                    }

                    datosRegistrados = true;
                    requiereRecalculo = true;
                    System.out.println();
                    System.out.println("[OK] Datos simulados generados. Estadisticas recalculadas.");
                    break;

                case 0:
                    System.out.println();
                    System.out.println("Saliendo del sistema. Hasta pronto.");
                    break;

                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }

            if (requiereRecalculo) {

                totalGeneral = 0;
                contadorAlertas = 0;
                equiposEncendidos = 0;

                for (i = 1; i <= NUM_EQUIPOS; i++) {
                    totalEquipo[i] = 0;
                    for (j = 1; j <= NUM_PERIODOS; j++) {
                        totalEquipo[i] = totalEquipo[i] + consumo[i][j];
                    }
                    totalGeneral = totalGeneral + totalEquipo[i];
                }

                promedioGeneral = totalGeneral / NUM_EQUIPOS;

                maxConsumo = totalEquipo[1];
                minConsumo = totalEquipo[1];
                posMax = 1;
                posMin = 1;
                for (i = 2; i <= NUM_EQUIPOS; i++) {
                    if (totalEquipo[i] > maxConsumo) {
                        maxConsumo = totalEquipo[i];
                        posMax = i;
                    }
                    if (totalEquipo[i] < minConsumo) {
                        minConsumo = totalEquipo[i];
                        posMin = i;
                    }
                }

                for (i = 1; i <= NUM_EQUIPOS; i++) {
                    if (totalGeneral > 0) {
                        porcentaje[i] = (totalEquipo[i] / totalGeneral) * 100;
                    } else {
                        porcentaje[i] = 0;
                    }
                    tendencia[i] = calcularTendencia(consumo[i][1], consumo[i][NUM_PERIODOS]);
                }

                if (promedioGeneral == 0) {
                    promedioGeneral = 0.0001;
                }

                for (i = 1; i <= NUM_EQUIPOS; i++) {
                    clasificacion[i] = clasificarConsumo(totalEquipo[i], promedioGeneral, FACTOR_BAJO, FACTOR_ALTO, FACTOR_CRITICO);
                }

                for (i = 1; i <= NUM_EQUIPOS; i++) {
                    alerta[i] = false;
                    if (estado[i].equals("Encendido")) {
                        equiposEncendidos = equiposEncendidos + 1;
                    }

                    if ((clasificacion[i].equals("ALTO") || clasificacion[i].equals("CRITICO")) && estado[i].equals("Encendido")) {
                        alerta[i] = true;
                        contadorAlertas = contadorAlertas + 1;
                    }
                }

                for (i = 1; i <= NUM_EQUIPOS; i++) {
                    recomendacion[i] = generarRecomendacion(nombresEquipos[i], porcentaje[i], clasificacion[i], estado[i]);
                }

                ordenarRanking(orden, totalEquipo, NUM_EQUIPOS);
            }

        } while (opcion != 0);

        System.out.println("==============================================================");
        System.out.println("                  FIN DEL PROGRAMA");
        System.out.println("==============================================================");

        sc.close();
    }

    static String clasificarConsumo(double consumoEquipo, double promedio, double factorBajo, double factorAlto, double factorCritico) {
        double ratio = consumoEquipo / promedio;
        String clase;

        if (ratio >= factorCritico) {
            clase = "CRITICO";
        } else if (ratio >= factorAlto) {
            clase = "ALTO";
        } else if (ratio >= factorBajo) {
            clase = "NORMAL";
        } else {
            clase = "BAJO";
        }

        return clase;
    }

    static String calcularTendencia(double valorInicial, double valorFinal) {
        String tend;
        if (valorFinal > valorInicial) {
            tend = "AUMENTO";
        } else if (valorFinal < valorInicial) {
            tend = "DISMINUCION";
        } else {
            tend = "ESTABLE";
        }
        return tend;
    }

    static String generarRecomendacion(String nombre, double porcentajeEquipo, String clase, String estadoEquipo) {
        String mensaje;

        if (clase.equals("CRITICO")) {
            mensaje = "Revisar de inmediato " + nombre + ": representa el " + porcentajeEquipo + "% del consumo total y esta en nivel CRITICO.";
        } else if (clase.equals("ALTO")) {
            mensaje = "Se recomienda reducir el tiempo de uso de " + nombre + ", ya que su consumo representa el " + porcentajeEquipo + "% del total, por encima del promedio.";
        } else if (clase.equals("BAJO")) {
            mensaje = nombre + " tiene un consumo BAJO (" + porcentajeEquipo + "% del total); no requiere acciones.";
        } else {
            mensaje = nombre + " funciona en nivel NORMAL (" + porcentajeEquipo + "% del total).";
        }

        if (estadoEquipo.equals("Apagado")) {
            mensaje = mensaje + " (Actualmente apagado).";
        }

        return mensaje;
    }

    static String convertirABinario(int numero) {
        int n = numero;
        String bin = "";

        if (n == 0) {
            bin = "0";
        } else {
            while (n > 0) {
                int resto = n % 2;
                bin = resto + bin;
                n = n / 2;
            }
        }
        return bin;
    }

    static void mostrarProcedimientoPromedio(double total, int cantidad, double promedio) {
        System.out.println("  Formula: Promedio = ConsumoTotal / CantidadEquipos");
        System.out.println("  Promedio = " + total + " / " + cantidad + " = " + promedio + " kWh");
    }

    static void mostrarConversionUnidades(double valorKWh) {
        double valorWh = valorKWh * 1000;
        double valorMWh = valorKWh / 1000;

        System.out.println("  Conversion de unidades del consumo total:");
        System.out.println("    " + valorKWh + " kWh = " + valorWh + " Wh");
        System.out.println("    " + valorKWh + " kWh = " + valorMWh + " MWh");
    }

    static void ordenarRanking(int[] orden, double[] totalEquipo, int n) {
        int temp;
        for (int i = 1; i <= n - 1; i++) {
            for (int j = 1; j <= n - i; j++) {
                if (totalEquipo[orden[j]] < totalEquipo[orden[j + 1]]) {
                    temp = orden[j];
                    orden[j] = orden[j + 1];
                    orden[j + 1] = temp;
                }
            }
        }
    }
}