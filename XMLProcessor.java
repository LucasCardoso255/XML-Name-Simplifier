import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLProcessor {

    public static void main(String[] args) {

        String dirPath = "C:\\Users\\User\\Desktop\\Nova pasta\\XML";
        File dir = new File(dirPath);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));
            if (files != null) {
                for (File file : files) {
                    System.out.println("Processando arquivo: " + file.getName());
                    try {
                        
                        processXML(file);
                    } catch (Exception e) {
                        System.out.println("Erro ao processar o arquivo: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Diretório não encontrado.");
        }
    }

    public static void processXML(File file) throws Exception {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList emitList = doc.getElementsByTagNameNS("http://www.portalfiscal.inf.br/nfe", "emit");
        String empresa = null;
        if (emitList.getLength() > 0) {
            Node emitNode = emitList.item(0);
            NodeList emitChildren = emitNode.getChildNodes();
            for (int i = 0; i < emitChildren.getLength(); i++) {
                Node child = emitChildren.item(i);
                if (child.getNodeName().equals("xNome")) {
                    empresa = child.getTextContent().trim();
                    break;
                }
            }
        }

        NodeList nNFList = doc.getElementsByTagNameNS("http://www.portalfiscal.inf.br/nfe", "nNF");
        String numeroNFe = null;
        if (nNFList.getLength() > 0) {
            numeroNFe = nNFList.item(0).getTextContent().trim();
        }

        NodeList dhEmiList = doc.getElementsByTagNameNS("http://www.portalfiscal.inf.br/nfe", "dhEmi");
        String dataEmissao = null;
        if (dhEmiList.getLength() > 0) {
            String dhEmi = dhEmiList.item(0).getTextContent();
            dataEmissao = dhEmi.substring(0, 10); // Pega apenas "YYYY-MM-DD"
        }

        if (empresa == null || numeroNFe == null || dataEmissao == null) {
            System.out.println("Erro: dados necessários não encontrados no XML.");
            return;
        }

        String dirEmpresa = file.getParent() + File.separator + empresa.replace(" ", "_");
        File pastaEmpresa = new File(dirEmpresa);
        if (!pastaEmpresa.exists()) {
            if (pastaEmpresa.mkdir()) {
                System.out.println("Diretório criado: " + dirEmpresa);
            } else {
                System.out.println("Falha ao criar diretório da empresa.");
                return;
            }
        }

        String novoNomeArquivo = dataEmissao + ".xml";
        File novoArquivo = new File(pastaEmpresa, novoNomeArquivo);

        if (file.renameTo(novoArquivo)) {
            System.out.println("Arquivo renomeado e movido para: " + novoArquivo.getAbsolutePath());
        } else {
            System.out.println("Falha ao renomear e mover o arquivo.");
        }
    }
}
