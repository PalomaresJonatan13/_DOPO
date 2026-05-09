package domain;

import java.io.File;

/**
 * Excepción de dominio para errores de persistencia e importación/exportación del bosque.
 */
public class ForestException extends Exception {
    /**
     * Mensaje estándar cuando falla abrir, guardar, importar o exportar.
     * @param option nombre de la opción de menú
     * @param fileName nombre del archivo
     * @return texto descriptivo
     */
    public static String FILE_METHODS_NOT_IMPLEMENTED(String option, String fileName) {
        return String.format("Opción '%s' en construcción. Archivo '%s'", option, fileName);
    }

    /**
     * Mensaje cuando la extensión del archivo no es la esperada.
     * @param option nombre de la opción
     * @param fileName nombre del archivo
     * @return texto descriptivo
     */
    public static String WRONG_EXTENSION(String option, String fileName) {
        return String.format("Error con la opción '%s', al usar el archivo '%s'. Se esperaba que el archivo tuviera otra extensión.", option, fileName);
    }

    /** Texto genérico de error de persistencia. */
    public static String PERSISTENCE_EXCEPTION = "Hubo un error con la opción y archivo seleccionado.";

    /**
     * Mensaje para token de objeto inválido en un .txt de importación.
     * @param obj identificador leído del archivo
     * @return texto descriptivo
     */
    public static String INVALID_OBJECT(String obj) {
        return String.format("The given object (%s) from the .txt file is invalid.", obj);
    }

    /**
     * Construye un mensaje con acción, ruta, tipo y detalle de la causa.
     * @param action etiqueta de la acción (p. ej. {@code "Abrir"})
     * @param file archivo involucrado
     * @param cause excepción original
     * @return mensaje listo para {@link #ForestException(String)}
     */
    public static String persistenceFailure(String action, File file, Throwable cause) {
        String path = file != null ? file.getAbsolutePath() : "(desconocido)";
        String type = cause.getClass().getName();
        String detail = cause.getMessage() != null ? cause.getMessage() : "";
        return String.format(
            "%s [acción=%s, archivo=%s, tipo=%s, detalle=%s]",
            PERSISTENCE_EXCEPTION, action, path, type, detail);
    }

    /**
     * Variante sin tipo de excepción incrustado (p. ej. error de formato de línea).
     * @param action etiqueta de la acción
     * @param file archivo involucrado
     * @return mensaje listo para {@link #ForestException(String)}
     */
    public static String persistenceFailure(String action, File file) {
        String path = file != null ? file.getAbsolutePath() : "(desconocido)";
        return String.format(
            "%s [acción=%s, archivo=%s]",
            PERSISTENCE_EXCEPTION, action, path);
    }

    /**
     * @param message descripción del error
     */
    public ForestException(String message) {
        super(message);
    }

    /**
     * @param message descripción del error
     * @param cause causa original
     */
    public ForestException(String message, Throwable cause) {
        super(message, cause);
    }
}
