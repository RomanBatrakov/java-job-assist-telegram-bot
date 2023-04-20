package com.batrakov.exeptions;

/**
 * Исключение, возникающее при ошибке загрузки файла.
 * Исключение наследуется от класса RuntimeException.
 * Класс содержит конструкторы с различными параметрами для передачи сообщения об ошибке и исключения-причины.
 *
 * @version 1.0
 */
public class UploadFileException extends RuntimeException {
    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(Throwable cause) {
        super(cause);
    }
}
