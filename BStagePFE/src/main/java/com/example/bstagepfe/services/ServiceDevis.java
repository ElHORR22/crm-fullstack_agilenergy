package com.example.bstagepfe.services;

import com.example.bstagepfe.DTO.DevisRequestDTO;
import com.example.bstagepfe.DTO.LigneDevisRequestDTO;
import com.example.bstagepfe.DTO.DevisResponseDTO;
import com.example.bstagepfe.DTO.LigneDevisResponseDTO;
import com.example.bstagepfe.entities.*;
import com.example.bstagepfe.repos.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ServiceDevis {

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private LigneDevisRepository ligneDevisRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private TVARepository tvaRepository;

    @Autowired
    private DevisCalculatorService calculator;

    @Transactional
    public DevisResponseDTO addDevisFromDto(DevisRequestDTO dto) {
        Devis devis = mapDtoToEntity(dto);
        attachLines(devis);
        calculator.compute(devis);

        Devis saved = devisRepository.save(devis);
        return toResponseDTO(saved);
    }

    public List<DevisResponseDTO> getAllDevisDto() {
        return devisRepository.findAllWithLignes()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public DevisResponseDTO getDevisDtoById(Long id) {
        Devis devis = devisRepository.findDevisById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devis non trouvé"));
        if (devis.getLigneDevis() != null) devis.getLigneDevis().size();
        return toResponseDTO(devis);
    }

    @Transactional
    public DevisResponseDTO updateDevisFromDto(Long id, DevisRequestDTO dto) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devis non trouvé"));

        Devis updated = mapDtoToEntity(dto);

        devis.setSujetDevis(updated.getSujetDevis());
        devis.setEcheance(updated.getEcheance());
        devis.setDelaiLivraison(updated.getDelaiLivraison());
        devis.setModeLivraison(updated.getModeLivraison());
        devis.setModePaiement(updated.getModePaiement());
        devis.setClient(updated.getClient());
        devis.setProspect(updated.getProspect());

        devis.getLigneDevis().clear();
        devis.getLigneDevis().addAll(updated.getLigneDevis());

        attachLines(devis);
        calculator.compute(devis);

        Devis saved = devisRepository.save(devis);
        return toResponseDTO(saved);
    }

    public void deleteDevis(Long id) {
        devisRepository.deleteById(id);
    }

    public List<Object[]> getCaDernierMois() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        return devisRepository.getChiffreAffairesByMonth(startDate);
    }

    public List<DevisResponseDTO> getDevisByEmailDto(String email) {
        return devisRepository.findDevisByClientOrProspectEmail(email)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void generatePDF(Long id, HttpServletResponse response) throws IOException, DocumentException {
        Optional<Devis> optionalDevis = devisRepository.findById(id);
        if (optionalDevis.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Devis non trouvé");
        }

        Devis devis = optionalDevis.get();
        Client client = devis.getClient();
        Prospect prospect = devis.getProspect();

        String nom = client != null ? client.getNom() : (prospect != null ? prospect.getNom() : "");
        String email = client != null ? client.getEmail() : (prospect != null ? prospect.getEmail() : "");
        String tel = client != null ? String.valueOf(client.getTelephone()) : (prospect != null ? String.valueOf(prospect.getTelephone()) : "");
        String adresse = client != null ? client.getAdresse() : (prospect != null ? prospect.getAdresse() : "");

        List<LigneDevis> lignes = ligneDevisRepository.findByDevisId(id);

        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=devis_" + id + ".pdf");
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 3});

        try {
            Image logo = Image.getInstance(new ClassPathResource("static/agilenergy.png").getURL());
            logo.scaleToFit(60, 60);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(logoCell);
        } catch (Exception e) {
            headerTable.addCell(new PdfPCell(new Phrase("")));
        }

        Font companyFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
        PdfPCell companyCell = new PdfPCell(new Phrase("SNDP Agil Energy", companyFont));
        companyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        companyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        companyCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(companyCell);
        document.add(headerTable);

        document.add(new Paragraph(" "));

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph(devis.getSujetDevis(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        Font infoFont = new Font(Font.FontFamily.HELVETICA, 11);
        Paragraph clientInfo = new Paragraph();
        clientInfo.setFont(infoFont);
        clientInfo.add("Destinataire : " + nom + "\n");
        clientInfo.add("Email : " + email + "\n");
        clientInfo.add("Téléphone : " + tel + "\n");
        clientInfo.add("Adresse : " + adresse + "\n");
        clientInfo.setSpacingAfter(10);
        document.add(clientInfo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (devis.getDateCreation() != null) {
            document.add(new Paragraph("Date d'émission : " + devis.getDateCreation().format(formatter), infoFont));
        }
        document.add(new Paragraph("Numéro de devis : #" + devis.getId(), infoFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{3, 2, 1.5f, 2, 1.5f, 2});

        Stream.of("Produit", "Emballage", "Quantité", "Prix U. HT", "TVA (%)", "Prix TTC")
                .forEach(col -> {
                    PdfPCell header = new PdfPCell(new Phrase(col, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPadding(6);
                    table.addCell(header);
                });

        boolean alternate = false;
        for (LigneDevis ligne : lignes) {
            BaseColor rowColor = alternate ? new BaseColor(245, 245, 245) : BaseColor.WHITE;
            alternate = !alternate;

            table.addCell(makeCell(ligne.getLibelleProduit(), rowColor));
            table.addCell(makeCell(ligne.getCodeEmballage(), rowColor));
            table.addCell(makeCell(formatDecimal(ligne.getQuantite()), rowColor));
            table.addCell(makeCell(formatDecimal(ligne.getPrixUnitaireHT()) + " TND", rowColor));
            table.addCell(makeCell(ligne.getTva() != null ? ligne.getTva().getValeur() + " %" : "N/A", rowColor));
            table.addCell(makeCell(formatDecimal(ligne.getPrixTTC()) + " TND", rowColor));
        }

        document.add(table);
        document.add(new Paragraph(" "));

        Font totalFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
        Paragraph total = new Paragraph("Total TTC : " + (devis.getTotalTTC() != null ? devis.getTotalTTC().toPlainString() : "0.00") + " TND", totalFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(10);
        document.add(total);

        document.add(new Paragraph(" "));

        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10);
        Paragraph footer = new Paragraph("Ce devis est valable pendant 15 jours à compter de sa date d'émission.", footerFont);
        footer.setAlignment(Element.ALIGN_LEFT);
        footer.setSpacingBefore(15);
        document.add(footer);

        document.add(new Paragraph(" "));

        PdfPTable signTable = new PdfPTable(2);
        signTable.setWidthPercentage(100);
        signTable.setSpacingBefore(30);
        signTable.setWidths(new float[]{1, 1});

        PdfPCell clientSig = new PdfPCell(new Phrase("Signature Client :", infoFont));
        clientSig.setBorder(Rectangle.NO_BORDER);
        clientSig.setPaddingTop(30);
        signTable.addCell(clientSig);

        PdfPCell societeCachet = new PdfPCell(new Phrase("Cachet et Signature Société :", infoFont));
        societeCachet.setBorder(Rectangle.NO_BORDER);
        societeCachet.setPaddingTop(30);
        signTable.addCell(societeCachet);

        document.add(signTable);

        document.close();
    }

    private PdfPCell makeCell(String content, BaseColor background) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private String formatDecimal(BigDecimal value) {
        return value != null ? value.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00";
    }


    private void attachLines(Devis devis) {
        if (devis.getLigneDevis() == null) return;
        devis.getLigneDevis().forEach(l -> l.setDevis(devis));
    }

    private Devis mapDtoToEntity(DevisRequestDTO dto) {
        Devis devis = new Devis();

        devis.setSujetDevis(dto.getSujetDevis());
        devis.setEcheance(dto.getEcheance());
        devis.setDelaiLivraison(dto.getDelaiLivraison());
        devis.setModeLivraison(dto.getModeLivraison());
        devis.setModePaiement(dto.getModePaiement());

        if (dto.getClientId() != null) {
            Client c = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client introuvable id=" + dto.getClientId()));
            devis.setClient(c);
            devis.setProspect(null);
        } else {
            Prospect p = prospectRepository.findById(dto.getProspectId())
                    .orElseThrow(() -> new EntityNotFoundException("Prospect introuvable id=" + dto.getProspectId()));
            devis.setProspect(p);
            devis.setClient(null);
        }

        TVA tvaUnique = tvaRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("TVA introuvable (id=1)."));

        List<LigneDevis> lignes = new ArrayList<>();
        for (LigneDevisRequestDTO ldto : dto.getLigneDevis()) {
            LigneDevis l = new LigneDevis();
            l.setCodeProduit(ldto.getCodeProduit());
            l.setLibelleProduit(ldto.getLibelleProduit());
            l.setCodeEmballage(ldto.getCodeEmballage());
            l.setQuantite(ldto.getQuantite());
            l.setPrixUnitaireHT(ldto.getPrixUnitaireHT());
            l.setEcoZit(ldto.getEcoZit());
            l.setPoidsLigneKg(ldto.getPoidsLigneKg());
            l.setTva(tvaUnique);

            lignes.add(l);
        }

        devis.setLigneDevis(lignes);
        return devis;
    }

    private DevisResponseDTO toResponseDTO(Devis devis) {
        DevisResponseDTO dto = new DevisResponseDTO();

        dto.setId(devis.getId());
        dto.setSujetDevis(devis.getSujetDevis());
        dto.setEcheance(devis.getEcheance());
        dto.setDelaiLivraison(devis.getDelaiLivraison());
        dto.setModeLivraison(devis.getModeLivraison());
        dto.setModePaiement(devis.getModePaiement());
        dto.setDateCreation(devis.getDateCreation());
        dto.setDateModification(devis.getDateModification());

        dto.setTotalTTC(devis.getTotalTTC());
        dto.setTotalPoidsKg(devis.getTotalPoidsKg());

        if (devis.getClient() != null) {
            dto.setClientId(devis.getClient().getId());
            dto.setNomClient(devis.getClient().getNom());
            dto.setProspectId(null);
            dto.setNomProspect("");
        } else if (devis.getProspect() != null) {
            dto.setProspectId(devis.getProspect().getId());
            dto.setNomProspect(devis.getProspect().getNom());
            dto.setClientId(null);
            dto.setNomClient("");
        }

        List<LigneDevisResponseDTO> lignes = new ArrayList<>();
        if (devis.getLigneDevis() != null) {
            for (LigneDevis l : devis.getLigneDevis()) {
                LigneDevisResponseDTO ld = new LigneDevisResponseDTO();
                ld.setId(l.getId());
                ld.setCodeProduit(l.getCodeProduit());
                ld.setLibelleProduit(l.getLibelleProduit());
                ld.setCodeEmballage(l.getCodeEmballage());
                ld.setQuantite(l.getQuantite());
                ld.setPrixUnitaireHT(l.getPrixUnitaireHT());
                ld.setEcoZit(l.getEcoZit());
                ld.setPrixTTC(l.getPrixTTC());
                ld.setPoidsLigneKg(l.getPoidsLigneKg());
                lignes.add(ld);
            }
        }
        dto.setLigneDevis(lignes);

        return dto;
    }
}
