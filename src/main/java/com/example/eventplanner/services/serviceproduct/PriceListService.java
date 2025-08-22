package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PriceListService {
    private final ServiceProductRepository serviceProductRepository;

    public Collection<PriceListDto> getBySppId(Long sppId) {
        return serviceProductRepository.getPriceListBySppId(sppId);
    }

    public PriceListDto update(Long id, Double price, Double discount) {
        return serviceProductRepository.findById(id)
                .map(sp -> {
                    sp.setPrice(price);
                    sp.setDiscount(discount);
                    ServiceProduct savedSp = serviceProductRepository.save(sp);
                    return ServiceProductMapper.toPriceListItemDto(savedSp);
                })
                .orElse(null);
    }

    public byte[] generatePdf(long id) {
        Collection<PriceListDto> priceList = getBySppId(id).isEmpty() ? Collections.emptyList() : getBySppId(id);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Price List Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20); // space below title
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            table.addCell(new PdfPCell(new Phrase("#", cellFont)));
            table.addCell(new PdfPCell(new Phrase("Name", cellFont)));
            table.addCell(new PdfPCell(new Phrase("Price", cellFont)));
            table.addCell(new PdfPCell(new Phrase("Discount", cellFont)));
            table.addCell(new PdfPCell(new Phrase("Total", cellFont)));

            int i = 1;
            for (PriceListDto sp : priceList) {
                table.addCell(String.valueOf(i++));
                table.addCell(sp.getName() != null ? sp.getName() : "");
                table.addCell(String.valueOf(sp.getPrice()));
                table.addCell(String.valueOf(sp.getDiscount()));
                table.addCell(String.valueOf(sp.getTotal()));
            }

            document.add(table);
            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
