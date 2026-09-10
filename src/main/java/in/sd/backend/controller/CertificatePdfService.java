package in.sd.backend.controller;

import in.sd.backend.model.Certificate;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificatePdfService {
    public byte[] create(Certificate c) {
        String name=safe(c.getStudent().getName()), course=safe(c.getCourse().getTitle()), id=safe(c.getCertificateId());
        String date=c.getIssuedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        List<String> lines=List.of("IT SUTRAA","CERTIFICATE OF COMPLETION","This certificate is proudly presented to",name,"for successfully completing the course",course,"Assessment Score: "+c.getScore()+" / "+c.getTotalQuestions(),"Certificate ID: "+id,"Issued: "+date,"Verify: /certificate/verify/"+id,"Learn  |  Build  |  Grow");
        List<String> streams=new ArrayList<>();
        streams.add("BT /F2 28 Tf 110 500 Td (IT SUTRAA) Tj ET");
        streams.add("BT /F2 24 Tf 245 450 Td (CERTIFICATE OF COMPLETION) Tj ET");
        streams.add("BT /F1 13 Tf 230 395 Td (This certificate is proudly presented to) Tj ET");
        streams.add("BT /F2 25 Tf 250 350 Td ("+pdf(name)+") Tj ET");
        streams.add("BT /F1 13 Tf 245 315 Td (for successfully completing the course) Tj ET");
        streams.add("BT /F2 20 Tf 250 275 Td ("+pdf(course)+") Tj ET");
        streams.add("BT /F1 12 Tf 270 220 Td (Assessment Score: "+c.getScore()+" / "+c.getTotalQuestions()+") Tj ET");
        streams.add("BT /F1 12 Tf 270 195 Td (Certificate ID: "+pdf(id)+") Tj ET");
        streams.add("BT /F1 12 Tf 270 170 Td (Issued: "+date+") Tj ET");
        streams.add("BT /F2 13 Tf 300 105 Td (Learn  |  Build  |  Grow) Tj ET");
        String content=String.join("\n",streams)+"\n";
        return pdfBytes(content);
    }
    private byte[] pdfBytes(String content){
        ByteArrayOutputStream out=new ByteArrayOutputStream(); List<Integer> offsets=new ArrayList<>(); write(out,"%PDF-1.4\n%IT SUTRAA\n");
        offsets.add(out.size()); write(out,"1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj\n");
        offsets.add(out.size()); write(out,"2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj\n");
        offsets.add(out.size()); write(out,"3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 842 595] /Resources << /Font << /F1 4 0 R /F2 5 0 R >> >> /Contents 6 0 R >> endobj\n");
        offsets.add(out.size()); write(out,"4 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> endobj\n");
        offsets.add(out.size()); write(out,"5 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >> endobj\n");
        offsets.add(out.size()); byte[] bytes=content.getBytes(StandardCharsets.ISO_8859_1); write(out,"6 0 obj << /Length "+bytes.length+" >> stream\n"); out.write(bytes,0,bytes.length); write(out,"endstream endobj\n");
        int xref=out.size(); write(out,"xref\n0 7\n0000000000 65535 f \n"); for(int off:offsets) write(out,String.format("%010d 00000 n \n",off)); write(out,"trailer << /Size 7 /Root 1 0 R >>\nstartxref\n"+xref+"\n%%EOF"); return out.toByteArray();
    }
    private void write(ByteArrayOutputStream o,String s){byte[] b=s.getBytes(StandardCharsets.ISO_8859_1);o.write(b,0,b.length);} private String safe(String s){return s==null?"":s.replaceAll("[^\\x20-\\x7E]","?");} private String pdf(String s){return s.replace("\\","\\\\").replace("(","\\(").replace(")","\\)");}
}
