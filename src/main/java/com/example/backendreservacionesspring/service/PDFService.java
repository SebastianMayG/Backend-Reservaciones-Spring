package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.model.User;
import com.example.backendreservacionesspring.repository.ReservationRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

@Service
public class PDFService {

    @Autowired
    private ReservationRepository reservationRepository; // Inyecta el repositorio

    public byte[] generatePdf(Assistance assistance) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Fuentes
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // 1. Título
        Paragraph title = new Paragraph("Reporte de Asistencia", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Obtener datos relacionados
        Reservation reservation = assistance.getReservation();
        Space sala = reservation.getSpace();
        User profesor = reservation.getUser();

        // 2. Información de la reserva
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(20f);

        addInfoRow(infoTable, "Fecha de clase:", assistance.getDateAssistance().toLocalDate().toString(), normalFont);
        addInfoRow(infoTable, "Sala:", sala.getName(), normalFont);
        addInfoRow(infoTable, "Descripción:", sala.getDescription(), normalFont);
        addInfoRow(infoTable, "Capacidad:", sala.getCapacity().toString(), normalFont);
        addInfoRow(infoTable, "Profesor:", profesor.getName() + " " + profesor.getLast_name(), normalFont);
        addInfoRow(infoTable, "Hora reservada:", reservation.getStartTime() + " - " + reservation.getEndTime(), normalFont);

        document.add(infoTable);

        // 3. Lista de estudiantes
        Paragraph subtitle = new Paragraph("Estudiantes Asistentes (" + assistance.getStudents().size() + ")", titleFont);
        subtitle.setSpacingAfter(10f);
        document.add(subtitle);

        PdfPTable studentTable = new PdfPTable(2);
        studentTable.setWidthPercentage(100);

        // Encabezados
        PdfPCell h1 = new PdfPCell(new Phrase("ID", headerFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Nombre", headerFont));
        h1.setBackgroundColor(BaseColor.DARK_GRAY);
        h2.setBackgroundColor(BaseColor.DARK_GRAY);
        studentTable.addCell(h1);
        studentTable.addCell(h2);

        // Datos
        assistance.getStudents().forEach(student -> {
            studentTable.addCell(new Phrase(student.getId().toString(), normalFont));
            studentTable.addCell(new Phrase(student.getName(), normalFont));
        });

        document.add(studentTable);
        document.close();
        return outputStream.toByteArray();
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBackgroundColor(new BaseColor(240, 240, 240)); // Gris claro
        labelCell.setBorderWidth(0.5f);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", font));
        valueCell.setBorderWidth(0.5f);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}