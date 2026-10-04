package io.sinistral.proteus.protocol;

/**
 * Media type registry with constants, file-extension lookups, and parsing.
 *
 * @author jbauer
 * @author Nikolche Mihajlovski (original Rapidoid implementation)
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

    /** Media type constant. */
public class MediaType {

    /** The file_extensions. */
    private static final Map<String, MediaType> FILE_EXTENSIONS =
        new LinkedHashMap<>();
    /** The no_attr. */
    private static final String[] NO_ATTR = new String[0];
    /** The utf8_attr. */
    private static final String[] UTF8_ATTR = { "charset=utf-8" };

    /*******************************************************/

    /** ANY media type constant. */
    public static final MediaType ANY = create("*/*");
    /** The {@code text/any} media type. */
    public static final MediaType TEXT_ANY = create("text/*");
    /** The {@code application/any} media type. */
    public static final MediaType APPLICATION_ANY = create("application/*");
    /** The {@code image/any} media type. */
    public static final MediaType IMAGE_ANY = create("image/*");
    /** The {@code video/any} media type. */
    public static final MediaType VIDEO_ANY = create("video/*");
    /** The {@code audio/any} media type. */
    public static final MediaType AUDIO_ANY = create("audio/*");

    /*******************************************************/

    /** The {@code application/yaml} media type. */
    public static final MediaType APPLICATION_YAML = create(
        "application/yaml",
        "yml",
        "yaml"
    );
    /** The {@code multipart/form-data} media type. */
    public static final MediaType MULTIPART_FORM_DATA = create(
        "multipart/form-data"
    );

    /** The {@code application/andrew-inset} media type. */
    public static final MediaType APPLICATION_ANDREW_INSET = create(
        "application/andrew-inset",
        "ez"
    );
    /** The {@code application/annodex} media type. */
    public static final MediaType APPLICATION_ANNODEX = create(
        "application/annodex",
        "anx"
    );
    /** The {@code application/applixware} media type. */
    public static final MediaType APPLICATION_APPLIXWARE = create(
        "application/applixware",
        "aw"
    );
    /** The {@code application/atomcat-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_ATOMCAT_XML_UTF8 = createUTF8(
        "application/atomcat+xml",
        "atomcat"
    );
    /** The {@code application/atomserv-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_ATOMSERV_XML_UTF8 = createUTF8(
        "application/atomserv+xml",
        "atomsrv"
    );
    /** The {@code application/atomsvc-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_ATOMSVC_XML_UTF8 = createUTF8(
        "application/atomsvc+xml",
        "atomsvc"
    );
    /** The {@code application/atom-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_ATOM_XML_UTF8 = createUTF8(
        "application/atom+xml",
        "atom"
    );
    /** The {@code application/bbolin} media type. */
    public static final MediaType APPLICATION_BBOLIN = create(
        "application/bbolin",
        "lin"
    );
    /** The {@code application/cap} media type. */
    public static final MediaType APPLICATION_CAP = create(
        "application/cap",
        "cap",
        "pcap"
    );
    /** The {@code application/ccxml-xml} media type. */
    public static final MediaType APPLICATION_CCXML_XML = create(
        "application/ccxml+xml",
        "ccxml"
    );
    /** The {@code application/cert-chain-cbor} media type. */
    public static final MediaType APPLICATION_CERT_CHAIN_CBOR = create(
        "application/cert-chain+cbor",
        "cbor"
    );
    /** The {@code application/cdmi-capability} media type. */
    public static final MediaType APPLICATION_CDMI_CAPABILITY = create(
        "application/cdmi-capability",
        "cdmia"
    );
    /** The {@code application/cdmi-container} media type. */
    public static final MediaType APPLICATION_CDMI_CONTAINER = create(
        "application/cdmi-container",
        "cdmic"
    );
    /** The {@code application/cdmi-domain} media type. */
    public static final MediaType APPLICATION_CDMI_DOMAIN = create(
        "application/cdmi-domain",
        "cdmid"
    );
    /** The {@code application/cdmi-object} media type. */
    public static final MediaType APPLICATION_CDMI_OBJECT = create(
        "application/cdmi-object",
        "cdmio"
    );
    /** The {@code application/cdmi-queue} media type. */
    public static final MediaType APPLICATION_CDMI_QUEUE = create(
        "application/cdmi-queue",
        "cdmiq"
    );
    /** The {@code application/cu-seeme} media type. */
    public static final MediaType APPLICATION_CU_SEEME = create(
        "application/cu-seeme",
        "cu"
    );
    /** The {@code application/davmount-xml} media type. */
    public static final MediaType APPLICATION_DAVMOUNT_XML = create(
        "application/davmount+xml",
        "davmount"
    );
    /** The {@code application/docbook-xml} media type. */
    public static final MediaType APPLICATION_DOCBOOK_XML = create(
        "application/docbook+xml",
        "dbk"
    );
    /** The {@code application/dsptype} media type. */
    public static final MediaType APPLICATION_DSPTYPE = create(
        "application/dsptype",
        "tsp"
    );
    /** The {@code application/dssc-der} media type. */
    public static final MediaType APPLICATION_DSSC_DER = create(
        "application/dssc+der",
        "dssc"
    );
    /** The {@code application/dssc-xml} media type. */
    public static final MediaType APPLICATION_DSSC_XML = create(
        "application/dssc+xml",
        "xdssc"
    );
    /** The {@code application/ecmascript with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_ECMASCRIPT_UTF8 = createUTF8(
        "application/ecmascript",
        "ecma",
        "es"
    );
    /** The {@code application/emma-xml} media type. */
    public static final MediaType APPLICATION_EMMA_XML = create(
        "application/emma+xml",
        "emma"
    );
    /** The {@code application/epub-zip} media type. */
    public static final MediaType APPLICATION_EPUB_ZIP = create(
        "application/epub+zip",
        "epub"
    );
    /** The {@code application/exi} media type. */
    public static final MediaType APPLICATION_EXI = create(
        "application/exi",
        "exi"
    );
    /** The {@code application/font-tdpfr} media type. */
    public static final MediaType APPLICATION_FONT_TDPFR = create(
        "application/font-tdpfr",
        "pfr"
    );
    /** The {@code application/futuresplash} media type. */
    public static final MediaType APPLICATION_FUTURESPLASH = create(
        "application/futuresplash",
        "spl"
    );
    /** The {@code application/gml-xml} media type. */
    public static final MediaType APPLICATION_GML_XML = create(
        "application/gml+xml",
        "gml"
    );
    /** The {@code application/gpx-xml} media type. */
    public static final MediaType APPLICATION_GPX_XML = create(
        "application/gpx+xml",
        "gpx"
    );
    /** The {@code application/gxf} media type. */
    public static final MediaType APPLICATION_GXF = create(
        "application/gxf",
        "gxf"
    );
    /** The {@code application/hta} media type. */
    public static final MediaType APPLICATION_HTA = create(
        "application/hta",
        "hta"
    );
    /** The {@code application/hyperstudio} media type. */
    public static final MediaType APPLICATION_HYPERSTUDIO = create(
        "application/hyperstudio",
        "stk"
    );
    /** The {@code application/inkml-xml} media type. */
    public static final MediaType APPLICATION_INKML_XML = create(
        "application/inkml+xml",
        "ink",
        "inkml"
    );
    /** The {@code application/ipfix} media type. */
    public static final MediaType APPLICATION_IPFIX = create(
        "application/ipfix",
        "ipfix"
    );
    /** The {@code application/java-archive} media type. */
    public static final MediaType APPLICATION_JAVA_ARCHIVE = create(
        "application/java-archive",
        "jar"
    );
    /** The {@code application/javascript with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_JAVASCRIPT_UTF8 = createUTF8(
        "application/javascript",
        "js"
    );
    /** The {@code application/java-serialized-object} media type. */
    public static final MediaType APPLICATION_JAVA_SERIALIZED_OBJECT = create(
        "application/java-serialized-object",
        "ser"
    );
    /** The {@code application/java-vm} media type. */
    public static final MediaType APPLICATION_JAVA_VM = create(
        "application/java-vm",
        "class"
    );
    /** The {@code application/json} media type. */
    public static final MediaType APPLICATION_JSON = create(
        "application/json",
        "json",
        "map"
    );
    /** The {@code application/jsonml-json} media type. */
    public static final MediaType APPLICATION_JSONML_JSON = create(
        "application/jsonml+json",
        "jsonml"
    );
    /** The {@code application/lost-xml} media type. */
    public static final MediaType APPLICATION_LOST_XML = create(
        "application/lost+xml",
        "lostxml"
    );
    /** The {@code application/m3g} media type. */
    public static final MediaType APPLICATION_M3G = create(
        "application/m3g",
        "m3g"
    );
    /** The {@code application/mac-binhex40} media type. */
    public static final MediaType APPLICATION_MAC_BINHEX40 = create(
        "application/mac-binhex40",
        "hqx"
    );
    /** The {@code application/mac-compactpro} media type. */
    public static final MediaType APPLICATION_MAC_COMPACTPRO = create(
        "application/mac-compactpro",
        "cpt"
    );
    /** The {@code application/mads-xml} media type. */
    public static final MediaType APPLICATION_MADS_XML = create(
        "application/mads+xml",
        "mads"
    );
    /** The {@code application/marc} media type. */
    public static final MediaType APPLICATION_MARC = create(
        "application/marc",
        "mrc"
    );
    /** The {@code application/marcxml-xml} media type. */
    public static final MediaType APPLICATION_MARCXML_XML = create(
        "application/marcxml+xml",
        "mrcx"
    );
    /** The {@code application/mathematica} media type. */
    public static final MediaType APPLICATION_MATHEMATICA = create(
        "application/mathematica",
        "ma",
        "mb",
        "nb",
        "nbp"
    );
    /** The {@code application/mathml-xml} media type. */
    public static final MediaType APPLICATION_MATHML_XML = create(
        "application/mathml+xml",
        "mathml"
    );
    /** The {@code application/mbox} media type. */
    public static final MediaType APPLICATION_MBOX = create(
        "application/mbox",
        "mbox"
    );
    /** The {@code application/mediaservercontrol-xml} media type. */
    public static final MediaType APPLICATION_MEDIASERVERCONTROL_XML = create(
        "application/mediaservercontrol+xml",
        "MSCML"
    );
    /** The {@code application/metalink4-xml} media type. */
    public static final MediaType APPLICATION_METALINK4_XML = create(
        "application/metalink4+xml",
        "meta4"
    );
    /** The {@code application/metalink-xml} media type. */
    public static final MediaType APPLICATION_METALINK_XML = create(
        "application/metalink+xml",
        "metalink"
    );
    /** The {@code application/mets-xml} media type. */
    public static final MediaType APPLICATION_METS_XML = create(
        "application/mets+xml",
        "mets"
    );
    /** The {@code application/mods-xml} media type. */
    public static final MediaType APPLICATION_MODS_XML = create(
        "application/mods+xml",
        "mods"
    );
    /** The {@code application/mp21} media type. */
    public static final MediaType APPLICATION_MP21 = create(
        "application/mp21",
        "m21",
        "mp21"
    );
    /** The {@code application/mp4} media type. */
    public static final MediaType APPLICATION_MP4 = create(
        "application/mp4",
        "mp4s"
    );
    /** The {@code application/msaccess} media type. */
    public static final MediaType APPLICATION_MSACCESS = create(
        "application/msaccess",
        "mdb"
    );
    /** The {@code application/msword} media type. */
    public static final MediaType APPLICATION_MSWORD = create(
        "application/msword",
        "doc",
        "dot"
    );
    /** The {@code application/mxf} media type. */
    public static final MediaType APPLICATION_MXF = create(
        "application/mxf",
        "mxf"
    );
    /** The {@code application/octet-stream} media type. */
    public static final MediaType APPLICATION_OCTET_STREAM = create(
        "application/octet-stream",
        "bin",
        "dms",
        "lrf",
        "MAR",
        "SO",
        "DIST",
        "DISTZ",
        "PKG",
        "BPK",
        "DUMP",
        "ELC",
        "DEPLOY"
    );
    /** The {@code application/oda} media type. */
    public static final MediaType APPLICATION_ODA = create(
        "application/oda",
        "oda"
    );
    /** The {@code application/oebps-package-xml} media type. */
    public static final MediaType APPLICATION_OEBPS_PACKAGE_XML = create(
        "application/oebps-package+xml",
        "opf"
    );
    /** The {@code application/ogg} media type. */
    public static final MediaType APPLICATION_OGG = create(
        "application/ogg",
        "ogx"
    );
    /** The {@code application/omdoc-xml} media type. */
    public static final MediaType APPLICATION_OMDOC_XML = create(
        "application/omdoc+xml",
        "omdoc"
    );
    /** The {@code application/onenote} media type. */
    public static final MediaType APPLICATION_ONENOTE = create(
        "application/onenote",
        "one",
        "onetoc",
        "onetoc2",
        "ONETMP",
        "ONEPKG"
    );
    /** The {@code application/oxps} media type. */
    public static final MediaType APPLICATION_OXPS = create(
        "application/oxps",
        "oxps"
    );
    /** The {@code application/patch-ops-error-xml} media type. */
    public static final MediaType APPLICATION_PATCH_OPS_ERROR_XML = create(
        "application/patch-ops-error+xml",
        "xer"
    );
    /** The {@code application/pdf} media type. */
    public static final MediaType APPLICATION_PDF = create(
        "application/pdf",
        "pdf"
    );
    /** The {@code application/pgp-encrypted} media type. */
    public static final MediaType APPLICATION_PGP_ENCRYPTED = create(
        "application/pgp-encrypted",
        "pgp"
    );
    /** The {@code application/pgp-keys} media type. */
    public static final MediaType APPLICATION_PGP_KEYS = create(
        "application/pgp-keys",
        "key"
    );
    /** The {@code application/pgp-signature} media type. */
    public static final MediaType APPLICATION_PGP_SIGNATURE = create(
        "application/pgp-signature",
        "asc",
        "pgp",
        "sig"
    );
    /** The {@code application/pics-rules} media type. */
    public static final MediaType APPLICATION_PICS_RULES = create(
        "application/pics-rules",
        "prf"
    );
    /** The {@code application/pkcs10} media type. */
    public static final MediaType APPLICATION_PKCS10 = create(
        "application/pkcs10",
        "p10"
    );
    /** The {@code application/pkcs7-mime} media type. */
    public static final MediaType APPLICATION_PKCS7_MIME = create(
        "application/pkcs7-mime",
        "p7m",
        "p7c"
    );
    /** The {@code application/pkcs7-signature} media type. */
    public static final MediaType APPLICATION_PKCS7_SIGNATURE = create(
        "application/pkcs7-signature",
        "p7s"
    );
    /** The {@code application/pkcs8} media type. */
    public static final MediaType APPLICATION_PKCS8 = create(
        "application/pkcs8",
        "p8"
    );
    /** The {@code application/pkix-attr-cert} media type. */
    public static final MediaType APPLICATION_PKIX_ATTR_CERT = create(
        "application/pkix-attr-cert",
        "ac"
    );
    /** The {@code application/pkix-cert} media type. */
    public static final MediaType APPLICATION_PKIX_CERT = create(
        "application/pkix-cert",
        "cer"
    );
    /** The {@code application/pkixcmp} media type. */
    public static final MediaType APPLICATION_PKIXCMP = create(
        "application/pkixcmp",
        "pki"
    );
    /** The {@code application/pkix-crl} media type. */
    public static final MediaType APPLICATION_PKIX_CRL = create(
        "application/pkix-crl",
        "crl"
    );
    /** The {@code application/pkix-pkipath} media type. */
    public static final MediaType APPLICATION_PKIX_PKIPATH = create(
        "application/pkix-pkipath",
        "pkipath"
    );
    /** The {@code application/pls-xml} media type. */
    public static final MediaType APPLICATION_PLS_XML = create(
        "application/pls+xml",
        "pls"
    );
    /** The {@code application/postscript} media type. */
    public static final MediaType APPLICATION_POSTSCRIPT = create(
        "application/postscript",
        "ps",
        "ai",
        "eps",
        "epsi",
        "EPSF",
        "EPS2",
        "EPS3"
    );
    /** The {@code application/prs-cww} media type. */
    public static final MediaType APPLICATION_PRS_CWW = create(
        "application/prs.cww",
        "cww"
    );
    /** The {@code application/pskc-xml} media type. */
    public static final MediaType APPLICATION_PSKC_XML = create(
        "application/pskc+xml",
        "pskcxml"
    );
    /** The {@code application/rar} media type. */
    public static final MediaType APPLICATION_RAR = create(
        "application/rar",
        "rar"
    );
    /** The {@code application/rdf-xml} media type. */
    public static final MediaType APPLICATION_RDF_XML = create(
        "application/rdf+xml",
        "rdf"
    );
    /** The {@code application/reginfo-xml} media type. */
    public static final MediaType APPLICATION_REGINFO_XML = create(
        "application/reginfo+xml",
        "rif"
    );
    /** The {@code application/relax-ng-compact-syntax} media type. */
    public static final MediaType APPLICATION_RELAX_NG_COMPACT_SYNTAX = create(
        "application/relax-ng-compact-syntax",
        "RNC"
    );
    /** The {@code application/resource-lists-diff-xml} media type. */
    public static final MediaType APPLICATION_RESOURCE_LISTS_DIFF_XML = create(
        "application/resource-lists-diff+xml",
        "RLD"
    );
    /** The {@code application/resource-lists-xml} media type. */
    public static final MediaType APPLICATION_RESOURCE_LISTS_XML = create(
        "application/resource-lists+xml",
        "rl"
    );
    /** The {@code application/rls-services-xml} media type. */
    public static final MediaType APPLICATION_RLS_SERVICES_XML = create(
        "application/rls-services+xml",
        "rs"
    );
    /** The {@code application/rpki-ghostbusters} media type. */
    public static final MediaType APPLICATION_RPKI_GHOSTBUSTERS = create(
        "application/rpki-ghostbusters",
        "gbr"
    );
    /** The {@code application/rpki-manifest} media type. */
    public static final MediaType APPLICATION_RPKI_MANIFEST = create(
        "application/rpki-manifest",
        "mft"
    );
    /** The {@code application/rpki-roa} media type. */
    public static final MediaType APPLICATION_RPKI_ROA = create(
        "application/rpki-roa",
        "roa"
    );
    /** The {@code application/rsd-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_RSD_XML_UTF8 = createUTF8(
        "application/rsd+xml",
        "rsd"
    );
    /** The {@code application/rss-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_RSS_XML_UTF8 = createUTF8(
        "application/rss+xml",
        "rss"
    );
    /** The {@code application/rtf} media type. */
    public static final MediaType APPLICATION_RTF = create(
        "application/rtf",
        "rtf"
    );
    /** The {@code application/sbml-xml} media type. */
    public static final MediaType APPLICATION_SBML_XML = create(
        "application/sbml+xml",
        "sbml"
    );
    /** The {@code application/scvp-cv-request} media type. */
    public static final MediaType APPLICATION_SCVP_CV_REQUEST = create(
        "application/scvp-cv-request",
        "scq"
    );
    /** The {@code application/scvp-cv-response} media type. */
    public static final MediaType APPLICATION_SCVP_CV_RESPONSE = create(
        "application/scvp-cv-response",
        "scs"
    );
    /** The {@code application/scvp-vp-request} media type. */
    public static final MediaType APPLICATION_SCVP_VP_REQUEST = create(
        "application/scvp-vp-request",
        "spq"
    );
    /** The {@code application/scvp-vp-response} media type. */
    public static final MediaType APPLICATION_SCVP_VP_RESPONSE = create(
        "application/scvp-vp-response",
        "spp"
    );
    /** The {@code application/sdp} media type. */
    public static final MediaType APPLICATION_SDP = create(
        "application/sdp",
        "sdp"
    );
    /** The {@code application/set-payment-initiation} media type. */
    public static final MediaType APPLICATION_SET_PAYMENT_INITIATION = create(
        "application/set-payment-initiation",
        "SETPAY"
    );
    /** The {@code application/set-registration-initiation} media type. */
    public static final MediaType APPLICATION_SET_REGISTRATION_INITIATION =
        create("APPLICATION/SET-REGISTRATION-INITIATION", "SETREG");
    /** The {@code application/shf-xml} media type. */
    public static final MediaType APPLICATION_SHF_XML = create(
        "application/signed-exchange;v=b3",
        "sxg"
    );
    /** The {@code application/signed-exchange} media type. */
    public static final MediaType APPLICATION_SIGNED_EXCHANGE = create(
        "application/shf+xml",
        "shf"
    );
    /** The {@code application/sla} media type. */
    public static final MediaType APPLICATION_SLA = create(
        "application/sla",
        "stl"
    );
    /** The {@code application/smil} media type. */
    public static final MediaType APPLICATION_SMIL = create(
        "application/smil",
        "smi",
        "smil"
    );
    /** The {@code application/smil-xml} media type. */
    public static final MediaType APPLICATION_SMIL_XML = create(
        "application/smil+xml",
        "smi",
        "smil"
    );
    /** The {@code application/sparql-query} media type. */
    public static final MediaType APPLICATION_SPARQL_QUERY = create(
        "application/sparql-query",
        "rq"
    );
    /** The {@code application/sparql-results-xml} media type. */
    public static final MediaType APPLICATION_SPARQL_RESULTS_XML = create(
        "application/sparql-results+xml",
        "srx"
    );
    /** The {@code application/srgs} media type. */
    public static final MediaType APPLICATION_SRGS = create(
        "application/srgs",
        "gram"
    );
    /** The {@code application/srgs-xml} media type. */
    public static final MediaType APPLICATION_SRGS_XML = create(
        "application/srgs+xml",
        "grxml"
    );
    /** The {@code application/sru-xml} media type. */
    public static final MediaType APPLICATION_SRU_XML = create(
        "application/sru+xml",
        "sru"
    );
    /** The {@code application/ssdl-xml} media type. */
    public static final MediaType APPLICATION_SSDL_XML = create(
        "application/ssdl+xml",
        "ssdl"
    );
    /** The {@code application/ssml-xml} media type. */
    public static final MediaType APPLICATION_SSML_XML = create(
        "application/ssml+xml",
        "ssml"
    );
    /** The {@code application/tei-xml} media type. */
    public static final MediaType APPLICATION_TEI_XML = create(
        "application/tei+xml",
        "tei",
        "teicorpus"
    );
    /** The {@code application/thraud-xml} media type. */
    public static final MediaType APPLICATION_THRAUD_XML = create(
        "application/thraud+xml",
        "tfi"
    );
    /** The {@code application/timestamped-data} media type. */
    public static final MediaType APPLICATION_TIMESTAMPED_DATA = create(
        "application/timestamped-data",
        "tsd"
    );
    /** The {@code application/vnd-3gpp2-tcap} media type. */
    public static final MediaType APPLICATION_VND_3GPP2_TCAP = create(
        "application/vnd.3gpp2.tcap",
        "tcap"
    );
    /** The {@code application/vnd-3gpp-pic-bw-large} media type. */
    public static final MediaType APPLICATION_VND_3GPP_PIC_BW_LARGE = create(
        "application/vnd.3gpp.pic-bw-large",
        "plb"
    );
    /** The {@code application/vnd-3gpp-pic-bw-small} media type. */
    public static final MediaType APPLICATION_VND_3GPP_PIC_BW_SMALL = create(
        "application/vnd.3gpp.pic-bw-small",
        "psb"
    );
    /** The {@code application/vnd-3gpp-pic-bw-var} media type. */
    public static final MediaType APPLICATION_VND_3GPP_PIC_BW_VAR = create(
        "application/vnd.3gpp.pic-bw-var",
        "pvb"
    );
    /** The {@code application/vnd-3m-post-it-notes} media type. */
    public static final MediaType APPLICATION_VND_3M_POST_IT_NOTES = create(
        "application/vnd.3m.post-it-notes",
        "pwn"
    );
    /** The {@code application/vnd-accpac-simply-aso} media type. */
    public static final MediaType APPLICATION_VND_ACCPAC_SIMPLY_ASO = create(
        "application/vnd.accpac.simply.aso",
        "aso"
    );
    /** The {@code application/vnd-accpac-simply-imp} media type. */
    public static final MediaType APPLICATION_VND_ACCPAC_SIMPLY_IMP = create(
        "application/vnd.accpac.simply.imp",
        "imp"
    );
    /** The {@code application/vnd-acucobol} media type. */
    public static final MediaType APPLICATION_VND_ACUCOBOL = create(
        "application/vnd.acucobol",
        "acu"
    );
    /** The {@code application/vnd-acucorp} media type. */
    public static final MediaType APPLICATION_VND_ACUCORP = create(
        "application/vnd.acucorp",
        "atc",
        "acutc"
    );
    /** The {@code application/vnd-adobe-air-application-installer-package-zip} media type. */
    public static final MediaType APPLICATION_VND_ADOBE_AIR_APPLICATION_INSTALLER_PACKAGE_ZIP =
        create(
            "APPLICATION/VND.ADOBE.AIR-APPLICATION-INSTALLER-PACKAGE+ZIP",
            "AIR"
        );
    /** The {@code application/vnd-adobe-formscentral-fcdt} media type. */
    public static final MediaType APPLICATION_VND_ADOBE_FORMSCENTRAL_FCDT =
        create("APPLICATION/VND.ADOBE.FORMSCENTRAL.FCDT", "FCDT");
    /** The {@code application/vnd-adobe-fxp} media type. */
    public static final MediaType APPLICATION_VND_ADOBE_FXP = create(
        "application/vnd.adobe.fxp",
        "fxp",
        "fxpl"
    );
    /** The {@code application/vnd-adobe-xdp-xml} media type. */
    public static final MediaType APPLICATION_VND_ADOBE_XDP_XML = create(
        "application/vnd.adobe.xdp+xml",
        "xdp"
    );
    /** The {@code application/vnd-adobe-xfdf} media type. */
    public static final MediaType APPLICATION_VND_ADOBE_XFDF = create(
        "application/vnd.adobe.xfdf",
        "xfdf"
    );
    /** The {@code application/vnd-ahead-space} media type. */
    public static final MediaType APPLICATION_VND_AHEAD_SPACE = create(
        "application/vnd.ahead.space",
        "ahead"
    );
    /** The {@code application/vnd-airzip-filesecure-azf} media type. */
    public static final MediaType APPLICATION_VND_AIRZIP_FILESECURE_AZF =
        create("application/vnd.airzip.filesecure.azf", "AZF");
    /** The {@code application/vnd-airzip-filesecure-azs} media type. */
    public static final MediaType APPLICATION_VND_AIRZIP_FILESECURE_AZS =
        create("application/vnd.airzip.filesecure.azs", "AZS");
    /** The {@code application/vnd-amazon-ebook} media type. */
    public static final MediaType APPLICATION_VND_AMAZON_EBOOK = create(
        "application/vnd.amazon.ebook",
        "azw"
    );
    /** The {@code application/vnd-americandynamics-acc} media type. */
    public static final MediaType APPLICATION_VND_AMERICANDYNAMICS_ACC = create(
        "application/vnd.americandynamics.acc",
        "ACC"
    );
    /** The {@code application/vnd-amiga-ami} media type. */
    public static final MediaType APPLICATION_VND_AMIGA_AMI = create(
        "application/vnd.amiga.ami",
        "ami"
    );
    /** The {@code application/vnd-android-package-archive} media type. */
    public static final MediaType APPLICATION_VND_ANDROID_PACKAGE_ARCHIVE =
        create("APPLICATION/VND.ANDROID.PACKAGE-ARCHIVE", "APK");
    /** The {@code application/vnd-anser-web-certificate-issue-initiation} media type. */
    public static final MediaType APPLICATION_VND_ANSER_WEB_CERTIFICATE_ISSUE_INITIATION =
        create("APPLICATION/VND.ANSER-WEB-CERTIFICATE-ISSUE-INITIATION", "CII");
    /** The {@code application/vnd-anser-web-funds-transfer-initiation} media type. */
    public static final MediaType APPLICATION_VND_ANSER_WEB_FUNDS_TRANSFER_INITIATION =
        create("APPLICATION/VND.ANSER-WEB-FUNDS-TRANSFER-INITIATION", "FTI");
    /** The {@code application/vnd-antix-game-component} media type. */
    public static final MediaType APPLICATION_VND_ANTIX_GAME_COMPONENT = create(
        "application/vnd.antix.game-component",
        "ATX"
    );
    /** The {@code application/vnd-apple-installer-xml} media type. */
    public static final MediaType APPLICATION_VND_APPLE_INSTALLER_XML = create(
        "application/vnd.apple.installer+xml",
        "MPKG"
    );
    /** The {@code application/vnd-apple-mpegurl} media type. */
    public static final MediaType APPLICATION_VND_APPLE_MPEGURL = create(
        "application/vnd.apple.mpegurl",
        "m3u8"
    );
    /** The {@code application/vnd-aristanetworks-swi} media type. */
    public static final MediaType APPLICATION_VND_ARISTANETWORKS_SWI = create(
        "application/vnd.aristanetworks.swi",
        "swi"
    );
    /** The {@code application/vnd-astraea-software-iota} media type. */
    public static final MediaType APPLICATION_VND_ASTRAEA_SOFTWARE_IOTA =
        create("application/vnd.astraea-software.iota", "IOTA");
    /** The {@code application/vnd-audiograph} media type. */
    public static final MediaType APPLICATION_VND_AUDIOGRAPH = create(
        "application/vnd.audiograph",
        "aep"
    );
    /** The {@code application/vnd-blueice-multipass} media type. */
    public static final MediaType APPLICATION_VND_BLUEICE_MULTIPASS = create(
        "application/vnd.blueice.multipass",
        "mpm"
    );
    /** The {@code application/vnd-bmi} media type. */
    public static final MediaType APPLICATION_VND_BMI = create(
        "application/vnd.bmi",
        "bmi"
    );
    /** The {@code application/vnd-businessobjects} media type. */
    public static final MediaType APPLICATION_VND_BUSINESSOBJECTS = create(
        "application/vnd.businessobjects",
        "rep"
    );
    /** The {@code application/vnd-chemdraw-xml} media type. */
    public static final MediaType APPLICATION_VND_CHEMDRAW_XML = create(
        "application/vnd.chemdraw+xml",
        "cdxml"
    );
    /** The {@code application/vnd-chipnuts-karaoke-mmd} media type. */
    public static final MediaType APPLICATION_VND_CHIPNUTS_KARAOKE_MMD = create(
        "application/vnd.chipnuts.karaoke-mmd",
        "MMD"
    );
    /** The {@code application/vnd-cinderella} media type. */
    public static final MediaType APPLICATION_VND_CINDERELLA = create(
        "application/vnd.cinderella",
        "cdy"
    );
    /** The {@code application/vnd-claymore} media type. */
    public static final MediaType APPLICATION_VND_CLAYMORE = create(
        "application/vnd.claymore",
        "cla"
    );
    /** The {@code application/vnd-cloanto-rp9} media type. */
    public static final MediaType APPLICATION_VND_CLOANTO_RP9 = create(
        "application/vnd.cloanto.rp9",
        "rp9"
    );
    /** The {@code application/vnd-clonk-c4group} media type. */
    public static final MediaType APPLICATION_VND_CLONK_C4GROUP = create(
        "application/vnd.clonk.c4group",
        "c4g",
        "c4d",
        "C4F",
        "C4P",
        "C4U"
    );
    /** The {@code application/vnd-cluetrust-cartomobile-config} media type. */
    public static final MediaType APPLICATION_VND_CLUETRUST_CARTOMOBILE_CONFIG =
        create("APPLICATION/VND.CLUETRUST.CARTOMOBILE-CONFIG", "C11AMC");
    /** The {@code application/vnd-cluetrust-cartomobile-config-pkg} media type. */
    public static final MediaType APPLICATION_VND_CLUETRUST_CARTOMOBILE_CONFIG_PKG =
        create("APPLICATION/VND.CLUETRUST.CARTOMOBILE-CONFIG-PKG", "C11AMZ");
    /** The {@code application/vnd-commonspace} media type. */
    public static final MediaType APPLICATION_VND_COMMONSPACE = create(
        "application/vnd.commonspace",
        "csp"
    );
    /** The {@code application/vnd-contact-cmsg} media type. */
    public static final MediaType APPLICATION_VND_CONTACT_CMSG = create(
        "application/vnd.contact.cmsg",
        "cdbcmsg"
    );
    /** The {@code application/vnd-cosmocaller} media type. */
    public static final MediaType APPLICATION_VND_COSMOCALLER = create(
        "application/vnd.cosmocaller",
        "cmc"
    );
    /** The {@code application/vnd-crick-clicker} media type. */
    public static final MediaType APPLICATION_VND_CRICK_CLICKER = create(
        "application/vnd.crick.clicker",
        "clkx"
    );
    /** The {@code application/vnd-crick-clicker-keyboard} media type. */
    public static final MediaType APPLICATION_VND_CRICK_CLICKER_KEYBOARD =
        create("APPLICATION/VND.CRICK.CLICKER.KEYBOARD", "CLKK");
    /** The {@code application/vnd-crick-clicker-palette} media type. */
    public static final MediaType APPLICATION_VND_CRICK_CLICKER_PALETTE =
        create("application/vnd.crick.clicker.palette", "CLKP");
    /** The {@code application/vnd-crick-clicker-template} media type. */
    public static final MediaType APPLICATION_VND_CRICK_CLICKER_TEMPLATE =
        create("APPLICATION/VND.CRICK.CLICKER.TEMPLATE", "CLKT");
    /** The {@code application/vnd-crick-clicker-wordbank} media type. */
    public static final MediaType APPLICATION_VND_CRICK_CLICKER_WORDBANK =
        create("APPLICATION/VND.CRICK.CLICKER.WORDBANK", "CLKW");
    /** The {@code application/vnd-criticaltools-wbs-xml} media type. */
    public static final MediaType APPLICATION_VND_CRITICALTOOLS_WBS_XML =
        create("application/vnd.criticaltools.wbs+xml", "WBS");
    /** The {@code application/vnd-ctc-posml} media type. */
    public static final MediaType APPLICATION_VND_CTC_POSML = create(
        "application/vnd.ctc-posml",
        "pml"
    );
    /** The {@code application/vnd-cups-ppd} media type. */
    public static final MediaType APPLICATION_VND_CUPS_PPD = create(
        "application/vnd.cups-ppd",
        "ppd"
    );
    /** The {@code application/vnd-curl-car} media type. */
    public static final MediaType APPLICATION_VND_CURL_CAR = create(
        "application/vnd.curl.car",
        "car"
    );
    /** The {@code application/vnd-curl-pcurl} media type. */
    public static final MediaType APPLICATION_VND_CURL_PCURL = create(
        "application/vnd.curl.pcurl",
        "pcurl"
    );
    /** The {@code application/vnd-dart} media type. */
    public static final MediaType APPLICATION_VND_DART = create(
        "application/vnd.dart",
        "dart"
    );
    /** The {@code application/vnd-data-vision-rdz} media type. */
    public static final MediaType APPLICATION_VND_DATA_VISION_RDZ = create(
        "application/vnd.data-vision.rdz",
        "rdz"
    );
    /** The {@code application/vnd-dece-data} media type. */
    public static final MediaType APPLICATION_VND_DECE_DATA = create(
        "application/vnd.dece.data",
        "uvf",
        "uvvf",
        "uvd",
        "UVVD"
    );
    /** The {@code application/vnd-dece-ttml-xml} media type. */
    public static final MediaType APPLICATION_VND_DECE_TTML_XML = create(
        "application/vnd.dece.ttml+xml",
        "uvt",
        "uvvt"
    );
    /** The {@code application/vnd-dece-unspecified} media type. */
    public static final MediaType APPLICATION_VND_DECE_UNSPECIFIED = create(
        "application/vnd.dece.unspecified",
        "uvx",
        "UVVX"
    );
    /** The {@code application/vnd-dece-zip} media type. */
    public static final MediaType APPLICATION_VND_DECE_ZIP = create(
        "application/vnd.dece.zip",
        "uvz",
        "uvvz"
    );
    /** The {@code application/vnd-denovo-fcselayout-link} media type. */
    public static final MediaType APPLICATION_VND_DENOVO_FCSELAYOUT_LINK =
        create("APPLICATION/VND.DENOVO.FCSELAYOUT-LINK", "FE_LAUNCH");
    /** The {@code application/vnd-dna} media type. */
    public static final MediaType APPLICATION_VND_DNA = create(
        "application/vnd.dna",
        "dna"
    );
    /** The {@code application/vnd-dolby-mlp} media type. */
    public static final MediaType APPLICATION_VND_DOLBY_MLP = create(
        "application/vnd.dolby.mlp",
        "mlp"
    );
    /** The {@code application/vnd-dpgraph} media type. */
    public static final MediaType APPLICATION_VND_DPGRAPH = create(
        "application/vnd.dpgraph",
        "dpg"
    );
    /** The {@code application/vnd-dreamfactory} media type. */
    public static final MediaType APPLICATION_VND_DREAMFACTORY = create(
        "application/vnd.dreamfactory",
        "dfac"
    );
    /** The {@code application/vnd-ds-keypoint} media type. */
    public static final MediaType APPLICATION_VND_DS_KEYPOINT = create(
        "application/vnd.ds-keypoint",
        "kpxx"
    );
    /** The {@code application/vnd-dvb-ait} media type. */
    public static final MediaType APPLICATION_VND_DVB_AIT = create(
        "application/vnd.dvb.ait",
        "ait"
    );
    /** The {@code application/vnd-dvb-service} media type. */
    public static final MediaType APPLICATION_VND_DVB_SERVICE = create(
        "application/vnd.dvb.service",
        "svc"
    );
    /** The {@code application/vnd-dynageo} media type. */
    public static final MediaType APPLICATION_VND_DYNAGEO = create(
        "application/vnd.dynageo",
        "geo"
    );
    /** The {@code application/vnd-ecowin-chart} media type. */
    public static final MediaType APPLICATION_VND_ECOWIN_CHART = create(
        "application/vnd.ecowin.chart",
        "mag"
    );
    /** The {@code application/vnd-enliven} media type. */
    public static final MediaType APPLICATION_VND_ENLIVEN = create(
        "application/vnd.enliven",
        "nml"
    );
    /** The {@code application/vnd-epson-esf} media type. */
    public static final MediaType APPLICATION_VND_EPSON_ESF = create(
        "application/vnd.epson.esf",
        "esf"
    );
    /** The {@code application/vnd-epson-msf} media type. */
    public static final MediaType APPLICATION_VND_EPSON_MSF = create(
        "application/vnd.epson.msf",
        "msf"
    );
    /** The {@code application/vnd-epson-quickanime} media type. */
    public static final MediaType APPLICATION_VND_EPSON_QUICKANIME = create(
        "application/vnd.epson.quickanime",
        "qam"
    );
    /** The {@code application/vnd-epson-salt} media type. */
    public static final MediaType APPLICATION_VND_EPSON_SALT = create(
        "application/vnd.epson.salt",
        "slt"
    );
    /** The {@code application/vnd-epson-ssf} media type. */
    public static final MediaType APPLICATION_VND_EPSON_SSF = create(
        "application/vnd.epson.ssf",
        "ssf"
    );
    /** The {@code application/vnd-eszigno3-xml} media type. */
    public static final MediaType APPLICATION_VND_ESZIGNO3_XML = create(
        "application/vnd.eszigno3+xml",
        "es3",
        "et3"
    );
    /** The {@code application/vnd-ezpix-album} media type. */
    public static final MediaType APPLICATION_VND_EZPIX_ALBUM = create(
        "application/vnd.ezpix-album",
        "ez2"
    );
    /** The {@code application/vnd-ezpix-package} media type. */
    public static final MediaType APPLICATION_VND_EZPIX_PACKAGE = create(
        "application/vnd.ezpix-package",
        "ez3"
    );
    /** The {@code application/vnd-fdf} media type. */
    public static final MediaType APPLICATION_VND_FDF = create(
        "application/vnd.fdf",
        "fdf"
    );
    /** The {@code application/vnd-fdsn-mseed} media type. */
    public static final MediaType APPLICATION_VND_FDSN_MSEED = create(
        "application/vnd.fdsn.mseed",
        "mseed"
    );
    /** The {@code application/vnd-fdsn-seed} media type. */
    public static final MediaType APPLICATION_VND_FDSN_SEED = create(
        "application/vnd.fdsn.seed",
        "seed",
        "dataless"
    );
    /** The {@code application/vnd-flographit} media type. */
    public static final MediaType APPLICATION_VND_FLOGRAPHIT = create(
        "application/vnd.flographit",
        "gph"
    );
    /** The {@code application/vnd-fluxtime-clip} media type. */
    public static final MediaType APPLICATION_VND_FLUXTIME_CLIP = create(
        "application/vnd.fluxtime.clip",
        "ftc"
    );
    /** The {@code application/vnd-framemaker} media type. */
    public static final MediaType APPLICATION_VND_FRAMEMAKER = create(
        "application/vnd.framemaker",
        "fm",
        "frame",
        "MAKER",
        "BOOK"
    );
    /** The {@code application/vnd-frogans-fnc} media type. */
    public static final MediaType APPLICATION_VND_FROGANS_FNC = create(
        "application/vnd.frogans.fnc",
        "fnc"
    );
    /** The {@code application/vnd-frogans-ltf} media type. */
    public static final MediaType APPLICATION_VND_FROGANS_LTF = create(
        "application/vnd.frogans.ltf",
        "ltf"
    );
    /** The {@code application/vnd-fsc-weblaunch} media type. */
    public static final MediaType APPLICATION_VND_FSC_WEBLAUNCH = create(
        "application/vnd.fsc.weblaunch",
        "fsc"
    );
    /** The {@code application/vnd-fujitsu-oasys2} media type. */
    public static final MediaType APPLICATION_VND_FUJITSU_OASYS2 = create(
        "application/vnd.fujitsu.oasys2",
        "oa2"
    );
    /** The {@code application/vnd-fujitsu-oasys3} media type. */
    public static final MediaType APPLICATION_VND_FUJITSU_OASYS3 = create(
        "application/vnd.fujitsu.oasys3",
        "oa3"
    );
    /** The {@code application/vnd-fujitsu-oasysgp} media type. */
    public static final MediaType APPLICATION_VND_FUJITSU_OASYSGP = create(
        "application/vnd.fujitsu.oasysgp",
        "fg5"
    );
    /** The {@code application/vnd-fujitsu-oasys} media type. */
    public static final MediaType APPLICATION_VND_FUJITSU_OASYS = create(
        "application/vnd.fujitsu.oasys",
        "oas"
    );
    /** The {@code application/vnd-fujitsu-oasysprs} media type. */
    public static final MediaType APPLICATION_VND_FUJITSU_OASYSPRS = create(
        "application/vnd.fujitsu.oasysprs",
        "bh2"
    );
    /** The {@code application/vnd-fujixerox-ddd} media type. */
    public static final MediaType APPLICATION_VND_FUJIXEROX_DDD = create(
        "application/vnd.fujixerox.ddd",
        "ddd"
    );
    /** The {@code application/vnd-fujixerox-docuworks-binder} media type. */
    public static final MediaType APPLICATION_VND_FUJIXEROX_DOCUWORKS_BINDER =
        create("APPLICATION/VND.FUJIXEROX.DOCUWORKS.BINDER", "XBD");
    /** The {@code application/vnd-fujixerox-docuworks} media type. */
    public static final MediaType APPLICATION_VND_FUJIXEROX_DOCUWORKS = create(
        "application/vnd.fujixerox.docuworks",
        "XDW"
    );
    /** The {@code application/vnd-fuzzysheet} media type. */
    public static final MediaType APPLICATION_VND_FUZZYSHEET = create(
        "application/vnd.fuzzysheet",
        "fzs"
    );
    /** The {@code application/vnd-genomatix-tuxedo} media type. */
    public static final MediaType APPLICATION_VND_GENOMATIX_TUXEDO = create(
        "application/vnd.genomatix.tuxedo",
        "txd"
    );
    /** The {@code application/vnd-geogebra-file} media type. */
    public static final MediaType APPLICATION_VND_GEOGEBRA_FILE = create(
        "application/vnd.geogebra.file",
        "ggb"
    );
    /** The {@code application/vnd-geogebra-tool} media type. */
    public static final MediaType APPLICATION_VND_GEOGEBRA_TOOL = create(
        "application/vnd.geogebra.tool",
        "ggt"
    );
    /** The {@code application/vnd-geometry-explorer} media type. */
    public static final MediaType APPLICATION_VND_GEOMETRY_EXPLORER = create(
        "application/vnd.geometry-explorer",
        "gex",
        "GRE"
    );
    /** The {@code application/vnd-geonext} media type. */
    public static final MediaType APPLICATION_VND_GEONEXT = create(
        "application/vnd.geonext",
        "gxt"
    );
    /** The {@code application/vnd-geoplan} media type. */
    public static final MediaType APPLICATION_VND_GEOPLAN = create(
        "application/vnd.geoplan",
        "g2w"
    );
    /** The {@code application/vnd-geospace} media type. */
    public static final MediaType APPLICATION_VND_GEOSPACE = create(
        "application/vnd.geospace",
        "g3w"
    );
    /** The {@code application/vnd-gmx} media type. */
    public static final MediaType APPLICATION_VND_GMX = create(
        "application/vnd.gmx",
        "gmx"
    );
    /** The {@code application/vnd-google-earth-kml-xml} media type. */
    public static final MediaType APPLICATION_VND_GOOGLE_EARTH_KML_XML = create(
        "application/vnd.google-earth.kml+xml",
        "KML"
    );
    /** The {@code application/vnd-google-earth-kmz} media type. */
    public static final MediaType APPLICATION_VND_GOOGLE_EARTH_KMZ = create(
        "application/vnd.google-earth.kmz",
        "kmz"
    );
    /** The {@code application/vnd-grafeq} media type. */
    public static final MediaType APPLICATION_VND_GRAFEQ = create(
        "application/vnd.grafeq",
        "gqf",
        "gqs"
    );
    /** The {@code application/vnd-groove-account} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_ACCOUNT = create(
        "application/vnd.groove-account",
        "gac"
    );
    /** The {@code application/vnd-groove-help} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_HELP = create(
        "application/vnd.groove-help",
        "ghf"
    );
    /** The {@code application/vnd-groove-identity-message} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_IDENTITY_MESSAGE =
        create("APPLICATION/VND.GROOVE-IDENTITY-MESSAGE", "GIM");
    /** The {@code application/vnd-groove-injector} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_INJECTOR = create(
        "application/vnd.groove-injector",
        "grv"
    );
    /** The {@code application/vnd-groove-tool-message} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_TOOL_MESSAGE = create(
        "application/vnd.groove-tool-message",
        "GTM"
    );
    /** The {@code application/vnd-groove-tool-template} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_TOOL_TEMPLATE = create(
        "application/vnd.groove-tool-template",
        "TPL"
    );
    /** The {@code application/vnd-groove-vcard} media type. */
    public static final MediaType APPLICATION_VND_GROOVE_VCARD = create(
        "application/vnd.groove-vcard",
        "vcg"
    );
    /** The {@code application/vnd-hal-xml} media type. */
    public static final MediaType APPLICATION_VND_HAL_XML = create(
        "application/vnd.hal+xml",
        "hal"
    );
    /** The {@code application/vnd-handheld-entertainment-xml} media type. */
    public static final MediaType APPLICATION_VND_HANDHELD_ENTERTAINMENT_XML =
        create("APPLICATION/VND.HANDHELD-ENTERTAINMENT+XML", "ZMM");
    /** The {@code application/vnd-hbci} media type. */
    public static final MediaType APPLICATION_VND_HBCI = create(
        "application/vnd.hbci",
        "hbci"
    );
    /** The {@code application/vnd-hhe-lesson-player} media type. */
    public static final MediaType APPLICATION_VND_HHE_LESSON_PLAYER = create(
        "application/vnd.hhe.lesson-player",
        "les"
    );
    /** The {@code application/vnd-hp-hpgl} media type. */
    public static final MediaType APPLICATION_VND_HP_HPGL = create(
        "application/vnd.hp-hpgl",
        "hpgl"
    );
    /** The {@code application/vnd-hp-hpid} media type. */
    public static final MediaType APPLICATION_VND_HP_HPID = create(
        "application/vnd.hp-hpid",
        "hpid"
    );
    /** The {@code application/vnd-hp-hps} media type. */
    public static final MediaType APPLICATION_VND_HP_HPS = create(
        "application/vnd.hp-hps",
        "hps"
    );
    /** The {@code application/vnd-hp-jlyt} media type. */
    public static final MediaType APPLICATION_VND_HP_JLYT = create(
        "application/vnd.hp-jlyt",
        "jlt"
    );
    /** The {@code application/vnd-hp-pcl} media type. */
    public static final MediaType APPLICATION_VND_HP_PCL = create(
        "application/vnd.hp-pcl",
        "pcl"
    );
    /** The {@code application/vnd-hp-pclxl} media type. */
    public static final MediaType APPLICATION_VND_HP_PCLXL = create(
        "application/vnd.hp-pclxl",
        "pclxl"
    );
    /** The {@code application/vnd-hydrostatix-sof-data} media type. */
    public static final MediaType APPLICATION_VND_HYDROSTATIX_SOF_DATA = create(
        "application/vnd.hydrostatix.sof-data",
        "SFD-HDSTX"
    );
    /** The {@code application/vnd-ibm-minipay} media type. */
    public static final MediaType APPLICATION_VND_IBM_MINIPAY = create(
        "application/vnd.ibm.minipay",
        "mpy"
    );
    /** The {@code application/vnd-ibm-modcap} media type. */
    public static final MediaType APPLICATION_VND_IBM_MODCAP = create(
        "application/vnd.ibm.modcap",
        "afp",
        "listafp",
        "LIST3820"
    );
    /** The {@code application/vnd-ibm-rights-management} media type. */
    public static final MediaType APPLICATION_VND_IBM_RIGHTS_MANAGEMENT =
        create("application/vnd.ibm.rights-management", "IRM");
    /** The {@code application/vnd-ibm-secure-container} media type. */
    public static final MediaType APPLICATION_VND_IBM_SECURE_CONTAINER = create(
        "application/vnd.ibm.secure-container",
        "SC"
    );
    /** The {@code application/vnd-iccprofile} media type. */
    public static final MediaType APPLICATION_VND_ICCPROFILE = create(
        "application/vnd.iccprofile",
        "icc",
        "icm"
    );
    /** The {@code application/vnd-igloader} media type. */
    public static final MediaType APPLICATION_VND_IGLOADER = create(
        "application/vnd.igloader",
        "igl"
    );
    /** The {@code application/vnd-immervision-ivp} media type. */
    public static final MediaType APPLICATION_VND_IMMERVISION_IVP = create(
        "application/vnd.immervision-ivp",
        "ivp"
    );
    /** The {@code application/vnd-immervision-ivu} media type. */
    public static final MediaType APPLICATION_VND_IMMERVISION_IVU = create(
        "application/vnd.immervision-ivu",
        "ivu"
    );
    /** The {@code application/vnd-insors-igm} media type. */
    public static final MediaType APPLICATION_VND_INSORS_IGM = create(
        "application/vnd.insors.igm",
        "igm"
    );
    /** The {@code application/vnd-intercon-formnet} media type. */
    public static final MediaType APPLICATION_VND_INTERCON_FORMNET = create(
        "application/vnd.intercon.formnet",
        "xpw",
        "XPX"
    );
    /** The {@code application/vnd-intergeo} media type. */
    public static final MediaType APPLICATION_VND_INTERGEO = create(
        "application/vnd.intergeo",
        "i2g"
    );
    /** The {@code application/vnd-intu-qbo} media type. */
    public static final MediaType APPLICATION_VND_INTU_QBO = create(
        "application/vnd.intu.qbo",
        "qbo"
    );
    /** The {@code application/vnd-intu-qfx} media type. */
    public static final MediaType APPLICATION_VND_INTU_QFX = create(
        "application/vnd.intu.qfx",
        "qfx"
    );
    /** The {@code application/vnd-ipunplugged-rcprofile} media type. */
    public static final MediaType APPLICATION_VND_IPUNPLUGGED_RCPROFILE =
        create("application/vnd.ipunplugged.rcprofile", "RCPROFILE");
    /** The {@code application/vnd-irepository-package-xml} media type. */
    public static final MediaType APPLICATION_VND_IREPOSITORY_PACKAGE_XML =
        create("APPLICATION/VND.IREPOSITORY.PACKAGE+XML", "IRP");
    /** The {@code application/vnd-isac-fcs} media type. */
    public static final MediaType APPLICATION_VND_ISAC_FCS = create(
        "application/vnd.isac.fcs",
        "fcs"
    );
    /** The {@code application/vnd-is-xpr} media type. */
    public static final MediaType APPLICATION_VND_IS_XPR = create(
        "application/vnd.is-xpr",
        "xpr"
    );
    /** The {@code application/vnd-jam} media type. */
    public static final MediaType APPLICATION_VND_JAM = create(
        "application/vnd.jam",
        "jam"
    );
    /** The {@code application/vnd-jcp-javame-midlet-rms} media type. */
    public static final MediaType APPLICATION_VND_JCP_JAVAME_MIDLET_RMS =
        create("application/vnd.jcp.javame.midlet-rms", "RMS");
    /** The {@code application/vnd-jisp} media type. */
    public static final MediaType APPLICATION_VND_JISP = create(
        "application/vnd.jisp",
        "jisp"
    );
    /** The {@code application/vnd-joost-joda-archive} media type. */
    public static final MediaType APPLICATION_VND_JOOST_JODA_ARCHIVE = create(
        "application/vnd.joost.joda-archive",
        "joda"
    );
    /** The {@code application/vnd-kahootz} media type. */
    public static final MediaType APPLICATION_VND_KAHOOTZ = create(
        "application/vnd.kahootz",
        "ktz",
        "ktr"
    );
    /** The {@code application/vnd-kde-karbon} media type. */
    public static final MediaType APPLICATION_VND_KDE_KARBON = create(
        "application/vnd.kde.karbon",
        "karbon"
    );
    /** The {@code application/vnd-kde-kchart} media type. */
    public static final MediaType APPLICATION_VND_KDE_KCHART = create(
        "application/vnd.kde.kchart",
        "chrt"
    );
    /** The {@code application/vnd-kde-kformula} media type. */
    public static final MediaType APPLICATION_VND_KDE_KFORMULA = create(
        "application/vnd.kde.kformula",
        "kfo"
    );
    /** The {@code application/vnd-kde-kivio} media type. */
    public static final MediaType APPLICATION_VND_KDE_KIVIO = create(
        "application/vnd.kde.kivio",
        "flw"
    );
    /** The {@code application/vnd-kde-kontour} media type. */
    public static final MediaType APPLICATION_VND_KDE_KONTOUR = create(
        "application/vnd.kde.kontour",
        "kon"
    );
    /** The {@code application/vnd-kde-kpresenter} media type. */
    public static final MediaType APPLICATION_VND_KDE_KPRESENTER = create(
        "application/vnd.kde.kpresenter",
        "kpr",
        "kpt"
    );
    /** The {@code application/vnd-kde-kspread} media type. */
    public static final MediaType APPLICATION_VND_KDE_KSPREAD = create(
        "application/vnd.kde.kspread",
        "ksp"
    );
    /** The {@code application/vnd-kde-kword} media type. */
    public static final MediaType APPLICATION_VND_KDE_KWORD = create(
        "application/vnd.kde.kword",
        "kwd",
        "kwt"
    );
    /** The {@code application/vnd-kenameaapp} media type. */
    public static final MediaType APPLICATION_VND_KENAMEAAPP = create(
        "application/vnd.kenameaapp",
        "htke"
    );
    /** The {@code application/vnd-kidspiration} media type. */
    public static final MediaType APPLICATION_VND_KIDSPIRATION = create(
        "application/vnd.kidspiration",
        "kia"
    );
    /** The {@code application/vnd-kinar} media type. */
    public static final MediaType APPLICATION_VND_KINAR = create(
        "application/vnd.kinar",
        "kne",
        "knp"
    );
    /** The {@code application/vnd-koan} media type. */
    public static final MediaType APPLICATION_VND_KOAN = create(
        "application/vnd.koan",
        "skp",
        "skd",
        "skt",
        "skm"
    );
    /** The {@code application/vnd-kodak-descriptor} media type. */
    public static final MediaType APPLICATION_VND_KODAK_DESCRIPTOR = create(
        "application/vnd.kodak-descriptor",
        "sse"
    );
    /** The {@code application/vnd-las-las-xml} media type. */
    public static final MediaType APPLICATION_VND_LAS_LAS_XML = create(
        "application/vnd.las.las+xml",
        "lasxml"
    );
    /** The {@code application/vnd-llamagraphics-life-balance-desktop} media type. */
    public static final MediaType APPLICATION_VND_LLAMAGRAPHICS_LIFE_BALANCE_DESKTOP =
        create("APPLICATION/VND.LLAMAGRAPHICS.LIFE-BALANCE.DESKTOP", "LBD");
    /** The {@code application/vnd-llamagraphics-life-balance-exchange-xml} media type. */
    public static final MediaType APPLICATION_VND_LLAMAGRAPHICS_LIFE_BALANCE_EXCHANGE_XML =
        create(
            "APPLICATION/VND.LLAMAGRAPHICS.LIFE-BALANCE.EXCHANGE+XML",
            "LBE"
        );
    /** The {@code application/vnd-lotus-1-2-3} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_1_2_3 = create(
        "application/vnd.lotus-1-2-3",
        "123"
    );
    /** The {@code application/vnd-lotus-approach} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_APPROACH = create(
        "application/vnd.lotus-approach",
        "apr"
    );
    /** The {@code application/vnd-lotus-freelance} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_FREELANCE = create(
        "application/vnd.lotus-freelance",
        "pre"
    );
    /** The {@code application/vnd-lotus-notes} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_NOTES = create(
        "application/vnd.lotus-notes",
        "nsf"
    );
    /** The {@code application/vnd-lotus-organizer} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_ORGANIZER = create(
        "application/vnd.lotus-organizer",
        "org"
    );
    /** The {@code application/vnd-lotus-screencam} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_SCREENCAM = create(
        "application/vnd.lotus-screencam",
        "scm"
    );
    /** The {@code application/vnd-lotus-wordpro} media type. */
    public static final MediaType APPLICATION_VND_LOTUS_WORDPRO = create(
        "application/vnd.lotus-wordpro",
        "lwp"
    );
    /** The {@code application/vnd-macports-portpkg} media type. */
    public static final MediaType APPLICATION_VND_MACPORTS_PORTPKG = create(
        "application/vnd.macports.portpkg",
        "portpkg"
    );
    /** The {@code application/vnd-mcd} media type. */
    public static final MediaType APPLICATION_VND_MCD = create(
        "application/vnd.mcd",
        "mcd"
    );
    /** The {@code application/vnd-medcalcdata} media type. */
    public static final MediaType APPLICATION_VND_MEDCALCDATA = create(
        "application/vnd.medcalcdata",
        "mc1"
    );
    /** The {@code application/vnd-mediastation-cdkey} media type. */
    public static final MediaType APPLICATION_VND_MEDIASTATION_CDKEY = create(
        "application/vnd.mediastation.cdkey",
        "CDKEY"
    );
    /** The {@code application/vnd-mfer} media type. */
    public static final MediaType APPLICATION_VND_MFER = create(
        "application/vnd.mfer",
        "mwf"
    );
    /** The {@code application/vnd-mfmp} media type. */
    public static final MediaType APPLICATION_VND_MFMP = create(
        "application/vnd.mfmp",
        "mfm"
    );
    /** The {@code application/vnd-micrografx-flo} media type. */
    public static final MediaType APPLICATION_VND_MICROGRAFX_FLO = create(
        "application/vnd.micrografx.flo",
        "flo"
    );
    /** The {@code application/vnd-micrografx-igx} media type. */
    public static final MediaType APPLICATION_VND_MICROGRAFX_IGX = create(
        "application/vnd.micrografx.igx",
        "igx"
    );
    /** The {@code application/vnd-mif} media type. */
    public static final MediaType APPLICATION_VND_MIF = create(
        "application/vnd.mif",
        "mif"
    );
    /** The {@code application/vnd-mobius-daf} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_DAF = create(
        "application/vnd.mobius.daf",
        "daf"
    );
    /** The {@code application/vnd-mobius-dis} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_DIS = create(
        "application/vnd.mobius.dis",
        "dis"
    );
    /** The {@code application/vnd-mobius-mbk} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_MBK = create(
        "application/vnd.mobius.mbk",
        "mbk"
    );
    /** The {@code application/vnd-mobius-mqy} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_MQY = create(
        "application/vnd.mobius.mqy",
        "mqy"
    );
    /** The {@code application/vnd-mobius-msl} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_MSL = create(
        "application/vnd.mobius.msl",
        "msl"
    );
    /** The {@code application/vnd-mobius-plc} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_PLC = create(
        "application/vnd.mobius.plc",
        "plc"
    );
    /** The {@code application/vnd-mobius-txf} media type. */
    public static final MediaType APPLICATION_VND_MOBIUS_TXF = create(
        "application/vnd.mobius.txf",
        "txf"
    );
    /** The {@code application/vnd-mophun-application} media type. */
    public static final MediaType APPLICATION_VND_MOPHUN_APPLICATION = create(
        "application/vnd.mophun.application",
        "mpn"
    );
    /** The {@code application/vnd-mophun-certificate} media type. */
    public static final MediaType APPLICATION_VND_MOPHUN_CERTIFICATE = create(
        "application/vnd.mophun.certificate",
        "mpc"
    );
    /** The {@code application/vnd-mozilla-xul-xml} media type. */
    public static final MediaType APPLICATION_VND_MOZILLA_XUL_XML = create(
        "application/vnd.mozilla.xul+xml",
        "xul"
    );
    /** The {@code application/vnd-ms-artgalry} media type. */
    public static final MediaType APPLICATION_VND_MS_ARTGALRY = create(
        "application/vnd.ms-artgalry",
        "cil"
    );
    /** The {@code application/vnd-ms-cab-compressed} media type. */
    public static final MediaType APPLICATION_VND_MS_CAB_COMPRESSED = create(
        "application/vnd.ms-cab-compressed",
        "cab"
    );
    /** The {@code application/vnd-mseq} media type. */
    public static final MediaType APPLICATION_VND_MSEQ = create(
        "application/vnd.mseq",
        "mseq"
    );
    /** The {@code application/vnd-ms-excel-addin-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_EXCEL_ADDIN_MACROENABLED_12 =
        create("APPLICATION/VND.MS-EXCEL.ADDIN.MACROENABLED.12", "XLAM");
    /** The {@code application/vnd-ms-excel-sheet-binary-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_EXCEL_SHEET_BINARY_MACROENABLED_12 =
        create("APPLICATION/VND.MS-EXCEL.SHEET.BINARY.MACROENABLED.12", "XLSB");
    /** The {@code application/vnd-ms-excel-sheet-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_EXCEL_SHEET_MACROENABLED_12 =
        create("APPLICATION/VND.MS-EXCEL.SHEET.MACROENABLED.12", "XLSM");
    /** The {@code application/vnd-ms-excel-template-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_EXCEL_TEMPLATE_MACROENABLED_12 =
        create("APPLICATION/VND.MS-EXCEL.TEMPLATE.MACROENABLED.12", "XLTM");
    /** The {@code application/vnd-ms-excel} media type. */
    public static final MediaType APPLICATION_VND_MS_EXCEL = create(
        "application/vnd.ms-excel",
        "xls",
        "xlm",
        "xla",
        "XLB",
        "XLC",
        "XLT",
        "XLW"
    );
    /** The {@code application/vnd-ms-fontobject} media type. */
    public static final MediaType APPLICATION_VND_MS_FONTOBJECT = create(
        "application/vnd.ms-fontobject",
        "eot"
    );
    /** The {@code application/vnd-ms-htmlhelp} media type. */
    public static final MediaType APPLICATION_VND_MS_HTMLHELP = create(
        "application/vnd.ms-htmlhelp",
        "chm"
    );
    /** The {@code application/vnd-ms-ims} media type. */
    public static final MediaType APPLICATION_VND_MS_IMS = create(
        "application/vnd.ms-ims",
        "ims"
    );
    /** The {@code application/vnd-ms-lrm} media type. */
    public static final MediaType APPLICATION_VND_MS_LRM = create(
        "application/vnd.ms-lrm",
        "lrm"
    );
    /** The {@code application/vnd-ms-officetheme} media type. */
    public static final MediaType APPLICATION_VND_MS_OFFICETHEME = create(
        "application/vnd.ms-officetheme",
        "thmx"
    );
    /** The {@code application/vnd-ms-pki-seccat} media type. */
    public static final MediaType APPLICATION_VND_MS_PKI_SECCAT = create(
        "application/vnd.ms-pki.seccat",
        "cat"
    );
    /** The {@code application/vnd-ms-pki-stl} media type. */
    public static final MediaType APPLICATION_VND_MS_PKI_STL = create(
        "application/vnd.ms-pki.stl",
        "stl"
    );
    /** The {@code application/vnd-ms-powerpoint-addin-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_POWERPOINT_ADDIN_MACROENABLED_12 =
        create("APPLICATION/VND.MS-POWERPOINT.ADDIN.MACROENABLED.12", "PPAM");
    /** The {@code application/vnd-ms-powerpoint} media type. */
    public static final MediaType APPLICATION_VND_MS_POWERPOINT = create(
        "application/vnd.ms-powerpoint",
        "ppt",
        "pps",
        "POT"
    );
    /** The {@code application/vnd-ms-powerpoint-presentation-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_POWERPOINT_PRESENTATION_MACROENABLED_12 =
        create(
            "APPLICATION/VND.MS-POWERPOINT.PRESENTATION.MACROENABLED.12",
            "PPTM"
        );
    /** The {@code application/vnd-ms-powerpoint-slide-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_POWERPOINT_SLIDE_MACROENABLED_12 =
        create("APPLICATION/VND.MS-POWERPOINT.SLIDE.MACROENABLED.12", "SLDM");
    /** The {@code application/vnd-ms-powerpoint-slideshow-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_POWERPOINT_SLIDESHOW_MACROENABLED_12 =
        create(
            "APPLICATION/VND.MS-POWERPOINT.SLIDESHOW.MACROENABLED.12",
            "PPSM"
        );
    /** The {@code application/vnd-ms-powerpoint-template-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_POWERPOINT_TEMPLATE_MACROENABLED_12 =
        create(
            "APPLICATION/VND.MS-POWERPOINT.TEMPLATE.MACROENABLED.12",
            "POTM"
        );
    /** The {@code application/vnd-ms-project} media type. */
    public static final MediaType APPLICATION_VND_MS_PROJECT = create(
        "application/vnd.ms-project",
        "mpp",
        "mpt"
    );
    /** The {@code application/vnd-ms-word-document-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_WORD_DOCUMENT_MACROENABLED_12 =
        create("APPLICATION/VND.MS-WORD.DOCUMENT.MACROENABLED.12", "DOCM");
    /** The {@code application/vnd-ms-word-template-macroenabled-12} media type. */
    public static final MediaType APPLICATION_VND_MS_WORD_TEMPLATE_MACROENABLED_12 =
        create("APPLICATION/VND.MS-WORD.TEMPLATE.MACROENABLED.12", "DOTM");
    /** The {@code application/vnd-ms-works} media type. */
    public static final MediaType APPLICATION_VND_MS_WORKS = create(
        "application/vnd.ms-works",
        "wps",
        "wks",
        "wcm",
        "wdb"
    );
    /** The {@code application/vnd-ms-wpl} media type. */
    public static final MediaType APPLICATION_VND_MS_WPL = create(
        "application/vnd.ms-wpl",
        "wpl"
    );
    /** The {@code application/vnd-ms-xpsdocument} media type. */
    public static final MediaType APPLICATION_VND_MS_XPSDOCUMENT = create(
        "application/vnd.ms-xpsdocument",
        "xps"
    );
    /** The {@code application/vnd-musician} media type. */
    public static final MediaType APPLICATION_VND_MUSICIAN = create(
        "application/vnd.musician",
        "mus"
    );
    /** The {@code application/vnd-muvee-style} media type. */
    public static final MediaType APPLICATION_VND_MUVEE_STYLE = create(
        "application/vnd.muvee.style",
        "msty"
    );
    /** The {@code application/vnd-mynfc} media type. */
    public static final MediaType APPLICATION_VND_MYNFC = create(
        "application/vnd.mynfc",
        "taglet"
    );
    /** The {@code application/vnd-neurolanguage-nlu} media type. */
    public static final MediaType APPLICATION_VND_NEUROLANGUAGE_NLU = create(
        "application/vnd.neurolanguage.nlu",
        "nlu"
    );
    /** The {@code application/vnd-nitf} media type. */
    public static final MediaType APPLICATION_VND_NITF = create(
        "application/vnd.nitf",
        "ntf",
        "nitf"
    );
    /** The {@code application/vnd-noblenet-directory} media type. */
    public static final MediaType APPLICATION_VND_NOBLENET_DIRECTORY = create(
        "application/vnd.noblenet-directory",
        "nnd"
    );
    /** The {@code application/vnd-noblenet-sealer} media type. */
    public static final MediaType APPLICATION_VND_NOBLENET_SEALER = create(
        "application/vnd.noblenet-sealer",
        "nns"
    );
    /** The {@code application/vnd-noblenet-web} media type. */
    public static final MediaType APPLICATION_VND_NOBLENET_WEB = create(
        "application/vnd.noblenet-web",
        "nnw"
    );
    /** The {@code application/vnd-nokia-n-gage-data} media type. */
    public static final MediaType APPLICATION_VND_NOKIA_N_GAGE_DATA = create(
        "application/vnd.nokia.n-gage.data",
        "ngdat"
    );
    /** The {@code application/vnd-nokia-n-gage-symbian-install} media type. */
    public static final MediaType APPLICATION_VND_NOKIA_N_GAGE_SYMBIAN_INSTALL =
        create("APPLICATION/VND.NOKIA.N-GAGE.SYMBIAN.INSTALL", "N-GAGE");
    /** The {@code application/vnd-nokia-radio-preset} media type. */
    public static final MediaType APPLICATION_VND_NOKIA_RADIO_PRESET = create(
        "application/vnd.nokia.radio-preset",
        "rpst"
    );
    /** The {@code application/vnd-nokia-radio-presets} media type. */
    public static final MediaType APPLICATION_VND_NOKIA_RADIO_PRESETS = create(
        "application/vnd.nokia.radio-presets",
        "RPSS"
    );
    /** The {@code application/vnd-novadigm-edm} media type. */
    public static final MediaType APPLICATION_VND_NOVADIGM_EDM = create(
        "application/vnd.novadigm.edm",
        "edm"
    );
    /** The {@code application/vnd-novadigm-edx} media type. */
    public static final MediaType APPLICATION_VND_NOVADIGM_EDX = create(
        "application/vnd.novadigm.edx",
        "edx"
    );
    /** The {@code application/vnd-novadigm-ext} media type. */
    public static final MediaType APPLICATION_VND_NOVADIGM_EXT = create(
        "application/vnd.novadigm.ext",
        "ext"
    );
    /** The {@code application/vnd-oasis-opendocument-chart} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_CHART =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.CHART", "ODC");
    /** The {@code application/vnd-oasis-opendocument-chart-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_CHART_TEMPLATE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.CHART-TEMPLATE", "OTC");
    /** The {@code application/vnd-oasis-opendocument-database} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_DATABASE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.DATABASE", "ODB");
    /** The {@code application/vnd-oasis-opendocument-formula} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_FORMULA =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.FORMULA", "ODF");
    /** The {@code application/vnd-oasis-opendocument-formula-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_FORMULA_TEMPLATE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.FORMULA-TEMPLATE", "ODFT");
    /** The {@code application/vnd-oasis-opendocument-graphics} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_GRAPHICS =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.GRAPHICS", "ODG");
    /** The {@code application/vnd-oasis-opendocument-graphics-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_GRAPHICS_TEMPLATE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.GRAPHICS-TEMPLATE", "OTG");
    /** The {@code application/vnd-oasis-opendocument-image} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_IMAGE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.IMAGE", "ODI");
    /** The {@code application/vnd-oasis-opendocument-image-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_IMAGE_TEMPLATE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.IMAGE-TEMPLATE", "OTI");
    /** The {@code application/vnd-oasis-opendocument-presentation} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.PRESENTATION", "ODP");
    /** The {@code application/vnd-oasis-opendocument-presentation-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION_TEMPLATE =
        create(
            "APPLICATION/VND.OASIS.OPENDOCUMENT.PRESENTATION-TEMPLATE",
            "OTP"
        );
    /** The {@code application/vnd-oasis-opendocument-spreadsheet} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.SPREADSHEET", "ODS");
    /** The {@code application/vnd-oasis-opendocument-spreadsheet-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET_TEMPLATE =
        create(
            "APPLICATION/VND.OASIS.OPENDOCUMENT.SPREADSHEET-TEMPLATE",
            "OTS"
        );
    /** The {@code application/vnd-oasis-opendocument-text-master} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT_MASTER =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.TEXT-MASTER", "ODM");
    /** The {@code application/vnd-oasis-opendocument-text} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.TEXT", "ODT");
    /** The {@code application/vnd-oasis-opendocument-text-template} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT_TEMPLATE =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.TEXT-TEMPLATE", "OTT");
    /** The {@code application/vnd-oasis-opendocument-text-web} media type. */
    public static final MediaType APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT_WEB =
        create("APPLICATION/VND.OASIS.OPENDOCUMENT.TEXT-WEB", "OTH");
    /** The {@code application/vnd-olpc-sugar} media type. */
    public static final MediaType APPLICATION_VND_OLPC_SUGAR = create(
        "application/vnd.olpc-sugar",
        "xo"
    );
    /** The {@code application/vnd-oma-dd2-xml} media type. */
    public static final MediaType APPLICATION_VND_OMA_DD2_XML = create(
        "application/vnd.oma.dd2+xml",
        "dd2"
    );
    /** The {@code application/vnd-openofficeorg-extension} media type. */
    public static final MediaType APPLICATION_VND_OPENOFFICEORG_EXTENSION =
        create("APPLICATION/VND.OPENOFFICEORG.EXTENSION", "OXT");
    /** The {@code application/vnd-openxmlformats-officedocument-presentationml-presentation} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.PRESENTATIONML.PRESENTATION",
            "PPTX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-presentationml-slideshow} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_SLIDESHOW =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.PRESENTATIONML.SLIDESHOW",
            "PPSX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-presentationml-slide} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_SLIDE =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.PRESENTATIONML.SLIDE",
            "SLDX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-presentationml-template} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_TEMPLATE =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.PRESENTATIONML.TEMPLATE",
            "POTX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-spreadsheetml-sheet} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.SPREADSHEETML.SHEET",
            "XLSX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-spreadsheetml-template} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_TEMPLATE =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.SPREADSHEETML.TEMPLATE",
            "XLTX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-wordprocessingml-document} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.WORDPROCESSINGML.DOCUMENT",
            "DOCX"
        );
    /** The {@code application/vnd-openxmlformats-officedocument-wordprocessingml-template} media type. */
    public static final MediaType APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_TEMPLATE =
        create(
            "APPLICATION/VND.OPENXMLFORMATS-OFFICEDOCUMENT.WORDPROCESSINGML.TEMPLATE",
            "DOTX"
        );
    /** The {@code application/vnd-osgeo-mapguide-package} media type. */
    public static final MediaType APPLICATION_VND_OSGEO_MAPGUIDE_PACKAGE =
        create("APPLICATION/VND.OSGEO.MAPGUIDE.PACKAGE", "MGP");
    /** The {@code application/vnd-osgi-dp} media type. */
    public static final MediaType APPLICATION_VND_OSGI_DP = create(
        "application/vnd.osgi.dp",
        "dp"
    );
    /** The {@code application/vnd-osgi-subsystem} media type. */
    public static final MediaType APPLICATION_VND_OSGI_SUBSYSTEM = create(
        "application/vnd.osgi.subsystem",
        "esa"
    );
    /** The {@code application/vnd-palm} media type. */
    public static final MediaType APPLICATION_VND_PALM = create(
        "application/vnd.palm",
        "pdb",
        "pqa",
        "oprc"
    );
    /** The {@code application/vnd-pawaafile} media type. */
    public static final MediaType APPLICATION_VND_PAWAAFILE = create(
        "application/vnd.pawaafile",
        "paw"
    );
    /** The {@code application/vnd-pg-format} media type. */
    public static final MediaType APPLICATION_VND_PG_FORMAT = create(
        "application/vnd.pg.format",
        "str"
    );
    /** The {@code application/vnd-pg-osasli} media type. */
    public static final MediaType APPLICATION_VND_PG_OSASLI = create(
        "application/vnd.pg.osasli",
        "ei6"
    );
    /** The {@code application/vnd-picsel} media type. */
    public static final MediaType APPLICATION_VND_PICSEL = create(
        "application/vnd.picsel",
        "efif"
    );
    /** The {@code application/vnd-pmi-widget} media type. */
    public static final MediaType APPLICATION_VND_PMI_WIDGET = create(
        "application/vnd.pmi.widget",
        "wg"
    );
    /** The {@code application/vnd-pocketlearn} media type. */
    public static final MediaType APPLICATION_VND_POCKETLEARN = create(
        "application/vnd.pocketlearn",
        "plf"
    );
    /** The {@code application/vnd-powerbuilder6} media type. */
    public static final MediaType APPLICATION_VND_POWERBUILDER6 = create(
        "application/vnd.powerbuilder6",
        "pbd"
    );
    /** The {@code application/vnd-previewsystems-box} media type. */
    public static final MediaType APPLICATION_VND_PREVIEWSYSTEMS_BOX = create(
        "application/vnd.previewsystems.box",
        "box"
    );
    /** The {@code application/vnd-proteus-magazine} media type. */
    public static final MediaType APPLICATION_VND_PROTEUS_MAGAZINE = create(
        "application/vnd.proteus.magazine",
        "mgz"
    );
    /** The {@code application/vnd-publishare-delta-tree} media type. */
    public static final MediaType APPLICATION_VND_PUBLISHARE_DELTA_TREE =
        create("application/vnd.publishare-delta-tree", "QPS");
    /** The {@code application/vnd-pvi-ptid1} media type. */
    public static final MediaType APPLICATION_VND_PVI_PTID1 = create(
        "application/vnd.pvi.ptid1",
        "ptid"
    );
    /** The {@code application/vnd-quark-quarkxpress} media type. */
    public static final MediaType APPLICATION_VND_QUARK_QUARKXPRESS = create(
        "application/vnd.quark.quarkxpress",
        "qxd",
        "QXT",
        "QWD",
        "QWT",
        "QXL",
        "QXB"
    );
    /** The {@code application/vnd-realvnc-bed} media type. */
    public static final MediaType APPLICATION_VND_REALVNC_BED = create(
        "application/vnd.realvnc.bed",
        "bed"
    );
    /** The {@code application/vnd-recordare-musicxml} media type. */
    public static final MediaType APPLICATION_VND_RECORDARE_MUSICXML = create(
        "application/vnd.recordare.musicxml",
        "mxl"
    );
    /** The {@code application/vnd-recordare-musicxml-xml} media type. */
    public static final MediaType APPLICATION_VND_RECORDARE_MUSICXML_XML =
        create("APPLICATION/VND.RECORDARE.MUSICXML+XML", "MUSICXML");
    /** The {@code application/vnd-rig-cryptonote} media type. */
    public static final MediaType APPLICATION_VND_RIG_CRYPTONOTE = create(
        "application/vnd.rig.cryptonote",
        "cryptonote"
    );
    /** The {@code application/vnd-rim-cod} media type. */
    public static final MediaType APPLICATION_VND_RIM_COD = create(
        "application/vnd.rim.cod",
        "cod"
    );
    /** The {@code application/vnd-rn-realmedia} media type. */
    public static final MediaType APPLICATION_VND_RN_REALMEDIA = create(
        "application/vnd.rn-realmedia",
        "rm"
    );
    /** The {@code application/vnd-rn-realmedia-vbr} media type. */
    public static final MediaType APPLICATION_VND_RN_REALMEDIA_VBR = create(
        "application/vnd.rn-realmedia-vbr",
        "rmvb"
    );
    /** The {@code application/vnd-route66-link66-xml} media type. */
    public static final MediaType APPLICATION_VND_ROUTE66_LINK66_XML = create(
        "application/vnd.route66.link66+xml",
        "LINK66"
    );
    /** The {@code application/vnd-sailingtracker-track} media type. */
    public static final MediaType APPLICATION_VND_SAILINGTRACKER_TRACK = create(
        "application/vnd.sailingtracker.track",
        "ST"
    );
    /** The {@code application/vnd-seemail} media type. */
    public static final MediaType APPLICATION_VND_SEEMAIL = create(
        "application/vnd.seemail",
        "see"
    );
    /** The {@code application/vnd-sema} media type. */
    public static final MediaType APPLICATION_VND_SEMA = create(
        "application/vnd.sema",
        "sema"
    );
    /** The {@code application/vnd-semd} media type. */
    public static final MediaType APPLICATION_VND_SEMD = create(
        "application/vnd.semd",
        "semd"
    );
    /** The {@code application/vnd-semf} media type. */
    public static final MediaType APPLICATION_VND_SEMF = create(
        "application/vnd.semf",
        "semf"
    );
    /** The {@code application/vnd-shana-informed-formdata} media type. */
    public static final MediaType APPLICATION_VND_SHANA_INFORMED_FORMDATA =
        create("APPLICATION/VND.SHANA.INFORMED.FORMDATA", "IFM");
    /** The {@code application/vnd-shana-informed-formtemplate} media type. */
    public static final MediaType APPLICATION_VND_SHANA_INFORMED_FORMTEMPLATE =
        create("APPLICATION/VND.SHANA.INFORMED.FORMTEMPLATE", "ITP");
    /** The {@code application/vnd-shana-informed-interchange} media type. */
    public static final MediaType APPLICATION_VND_SHANA_INFORMED_INTERCHANGE =
        create("APPLICATION/VND.SHANA.INFORMED.INTERCHANGE", "IIF");
    /** The {@code application/vnd-shana-informed-package} media type. */
    public static final MediaType APPLICATION_VND_SHANA_INFORMED_PACKAGE =
        create("APPLICATION/VND.SHANA.INFORMED.PACKAGE", "IPK");
    /** The {@code application/vnd-simtech-mindmapper} media type. */
    public static final MediaType APPLICATION_VND_SIMTECH_MINDMAPPER = create(
        "application/vnd.simtech-mindmapper",
        "twd",
        "TWDS"
    );
    /** The {@code application/vnd-smaf} media type. */
    public static final MediaType APPLICATION_VND_SMAF = create(
        "application/vnd.smaf",
        "mmf"
    );
    /** The {@code application/vnd-smart-teacher} media type. */
    public static final MediaType APPLICATION_VND_SMART_TEACHER = create(
        "application/vnd.smart.teacher",
        "teacher"
    );
    /** The {@code application/vnd-solent-sdkm-xml} media type. */
    public static final MediaType APPLICATION_VND_SOLENT_SDKM_XML = create(
        "application/vnd.solent.sdkm+xml",
        "sdkm",
        "SDKD"
    );
    /** The {@code application/vnd-spotfire-dxp} media type. */
    public static final MediaType APPLICATION_VND_SPOTFIRE_DXP = create(
        "application/vnd.spotfire.dxp",
        "dxp"
    );
    /** The {@code application/vnd-spotfire-sfs} media type. */
    public static final MediaType APPLICATION_VND_SPOTFIRE_SFS = create(
        "application/vnd.spotfire.sfs",
        "sfs"
    );
    /** The {@code application/vnd-stardivision-calc} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_CALC = create(
        "application/vnd.stardivision.calc",
        "sdc"
    );
    /** The {@code application/vnd-stardivision-chart} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_CHART = create(
        "application/vnd.stardivision.chart",
        "sds"
    );
    /** The {@code application/vnd-stardivision-draw} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_DRAW = create(
        "application/vnd.stardivision.draw",
        "sda"
    );
    /** The {@code application/vnd-stardivision-impress} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_IMPRESS = create(
        "application/vnd.stardivision.impress",
        "SDD"
    );
    /** The {@code application/vnd-stardivision-math} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_MATH = create(
        "application/vnd.stardivision.math",
        "smf",
        "SDF"
    );
    /** The {@code application/vnd-stardivision-writer-global} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_WRITER_GLOBAL =
        create("APPLICATION/VND.STARDIVISION.WRITER-GLOBAL", "SGL");
    /** The {@code application/vnd-stardivision-writer} media type. */
    public static final MediaType APPLICATION_VND_STARDIVISION_WRITER = create(
        "application/vnd.stardivision.writer",
        "SDW",
        "VOR"
    );
    /** The {@code application/vnd-stepmania-package} media type. */
    public static final MediaType APPLICATION_VND_STEPMANIA_PACKAGE = create(
        "application/vnd.stepmania.package",
        "smzip"
    );
    /** The {@code application/vnd-stepmania-stepchart} media type. */
    public static final MediaType APPLICATION_VND_STEPMANIA_STEPCHART = create(
        "application/vnd.stepmania.stepchart",
        "sm"
    );
    /** The {@code application/vnd-sun-xml-calc} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_CALC = create(
        "application/vnd.sun.xml.calc",
        "sxc"
    );
    /** The {@code application/vnd-sun-xml-calc-template} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_CALC_TEMPLATE =
        create("application/vnd.sun.xml.calc.template", "STC");
    /** The {@code application/vnd-sun-xml-draw} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_DRAW = create(
        "application/vnd.sun.xml.draw",
        "sxd"
    );
    /** The {@code application/vnd-sun-xml-draw-template} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_DRAW_TEMPLATE =
        create("application/vnd.sun.xml.draw.template", "STD");
    /** The {@code application/vnd-sun-xml-impress} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_IMPRESS = create(
        "application/vnd.sun.xml.impress",
        "sxi"
    );
    /** The {@code application/vnd-sun-xml-impress-template} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_IMPRESS_TEMPLATE =
        create("APPLICATION/VND.SUN.XML.IMPRESS.TEMPLATE", "STI");
    /** The {@code application/vnd-sun-xml-math} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_MATH = create(
        "application/vnd.sun.xml.math",
        "sxm"
    );
    /** The {@code application/vnd-sun-xml-writer} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_WRITER = create(
        "application/vnd.sun.xml.writer",
        "sxw"
    );
    /** The {@code application/vnd-sun-xml-writer-template} media type. */
    public static final MediaType APPLICATION_VND_SUN_XML_WRITER_TEMPLATE =
        create("APPLICATION/VND.SUN.XML.WRITER.TEMPLATE", "STW");
    /** The {@code application/vnd-sus-calendar} media type. */
    public static final MediaType APPLICATION_VND_SUS_CALENDAR = create(
        "application/vnd.sus-calendar",
        "sus",
        "susp"
    );
    /** The {@code application/vnd-svd} media type. */
    public static final MediaType APPLICATION_VND_SVD = create(
        "application/vnd.svd",
        "svd"
    );
    /** The {@code application/vnd-symbian-install} media type. */
    public static final MediaType APPLICATION_VND_SYMBIAN_INSTALL = create(
        "application/vnd.symbian.install",
        "sis",
        "SISX"
    );
    /** The {@code application/vnd-syncml-dm-wbxml} media type. */
    public static final MediaType APPLICATION_VND_SYNCML_DM_WBXML = create(
        "application/vnd.syncml.dm+wbxml",
        "bdm"
    );
    /** The {@code application/vnd-syncml-dm-xml} media type. */
    public static final MediaType APPLICATION_VND_SYNCML_DM_XML = create(
        "application/vnd.syncml.dm+xml",
        "xdm"
    );
    /** The {@code application/vnd-syncml-xml} media type. */
    public static final MediaType APPLICATION_VND_SYNCML_XML = create(
        "application/vnd.syncml+xml",
        "xsm"
    );
    /** The {@code application/vnd-tao-intent-module-archive} media type. */
    public static final MediaType APPLICATION_VND_TAO_INTENT_MODULE_ARCHIVE =
        create("APPLICATION/VND.TAO.INTENT-MODULE-ARCHIVE", "TAO");
    /** The {@code application/vnd-tcpdump-pcap} media type. */
    public static final MediaType APPLICATION_VND_TCPDUMP_PCAP = create(
        "application/vnd.tcpdump.pcap",
        "pcap",
        "cap",
        "DMP"
    );
    /** The {@code application/vnd-tmobile-livetv} media type. */
    public static final MediaType APPLICATION_VND_TMOBILE_LIVETV = create(
        "application/vnd.tmobile-livetv",
        "tmo"
    );
    /** The {@code application/vnd-trid-tpt} media type. */
    public static final MediaType APPLICATION_VND_TRID_TPT = create(
        "application/vnd.trid.tpt",
        "tpt"
    );
    /** The {@code application/vnd-triscape-mxs} media type. */
    public static final MediaType APPLICATION_VND_TRISCAPE_MXS = create(
        "application/vnd.triscape.mxs",
        "mxs"
    );
    /** The {@code application/vnd-trueapp} media type. */
    public static final MediaType APPLICATION_VND_TRUEAPP = create(
        "application/vnd.trueapp",
        "tra"
    );
    /** The {@code application/vnd-ufdl} media type. */
    public static final MediaType APPLICATION_VND_UFDL = create(
        "application/vnd.ufdl",
        "ufd",
        "ufdl"
    );
    /** The {@code application/vnd-uiq-theme} media type. */
    public static final MediaType APPLICATION_VND_UIQ_THEME = create(
        "application/vnd.uiq.theme",
        "utz"
    );
    /** The {@code application/vnd-umajin} media type. */
    public static final MediaType APPLICATION_VND_UMAJIN = create(
        "application/vnd.umajin",
        "umj"
    );
    /** The {@code application/vnd-unity} media type. */
    public static final MediaType APPLICATION_VND_UNITY = create(
        "application/vnd.unity",
        "unityweb"
    );
    /** The {@code application/vnd-uoml-xml} media type. */
    public static final MediaType APPLICATION_VND_UOML_XML = create(
        "application/vnd.uoml+xml",
        "uoml"
    );
    /** The {@code application/vnd-vcx} media type. */
    public static final MediaType APPLICATION_VND_VCX = create(
        "application/vnd.vcx",
        "vcx"
    );
    /** The {@code application/vnd-visionary} media type. */
    public static final MediaType APPLICATION_VND_VISIONARY = create(
        "application/vnd.visionary",
        "vis"
    );
    /** The {@code application/vnd-visio} media type. */
    public static final MediaType APPLICATION_VND_VISIO = create(
        "application/vnd.visio",
        "vsd",
        "vst",
        "vss",
        "vsw"
    );
    /** The {@code application/vnd-vsf} media type. */
    public static final MediaType APPLICATION_VND_VSF = create(
        "application/vnd.vsf",
        "vsf"
    );
    /** The {@code application/vnd-wap-wbxml} media type. */
    public static final MediaType APPLICATION_VND_WAP_WBXML = create(
        "application/vnd.wap.wbxml",
        "wbxml"
    );
    /** The {@code application/vnd-wap-wmlc} media type. */
    public static final MediaType APPLICATION_VND_WAP_WMLC = create(
        "application/vnd.wap.wmlc",
        "wmlc"
    );
    /** The {@code application/vnd-wap-wmlscriptc} media type. */
    public static final MediaType APPLICATION_VND_WAP_WMLSCRIPTC = create(
        "application/vnd.wap.wmlscriptc",
        "wmlsc"
    );
    /** The {@code application/vnd-webturbo} media type. */
    public static final MediaType APPLICATION_VND_WEBTURBO = create(
        "application/vnd.webturbo",
        "wtb"
    );
    /** The {@code application/vnd-wolfram-player} media type. */
    public static final MediaType APPLICATION_VND_WOLFRAM_PLAYER = create(
        "application/vnd.wolfram.player",
        "nbp"
    );
    /** The {@code application/vnd-wordperfect5-1} media type. */
    public static final MediaType APPLICATION_VND_WORDPERFECT5_1 = create(
        "application/vnd.wordperfect5.1",
        "wp5"
    );
    /** The {@code application/vnd-wordperfect} media type. */
    public static final MediaType APPLICATION_VND_WORDPERFECT = create(
        "application/vnd.wordperfect",
        "wpd"
    );
    /** The {@code application/vnd-wqd} media type. */
    public static final MediaType APPLICATION_VND_WQD = create(
        "application/vnd.wqd",
        "wqd"
    );
    /** The {@code application/vnd-wt-stf} media type. */
    public static final MediaType APPLICATION_VND_WT_STF = create(
        "application/vnd.wt.stf",
        "stf"
    );
    /** The {@code application/vnd-xara} media type. */
    public static final MediaType APPLICATION_VND_XARA = create(
        "application/vnd.xara",
        "xar"
    );
    /** The {@code application/vnd-xfdl} media type. */
    public static final MediaType APPLICATION_VND_XFDL = create(
        "application/vnd.xfdl",
        "xfdl"
    );
    /** The {@code application/vnd-yamaha-hv-dic} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_HV_DIC = create(
        "application/vnd.yamaha.hv-dic",
        "hvd"
    );
    /** The {@code application/vnd-yamaha-hv-script} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_HV_SCRIPT = create(
        "application/vnd.yamaha.hv-script",
        "hvs"
    );
    /** The {@code application/vnd-yamaha-hv-voice} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_HV_VOICE = create(
        "application/vnd.yamaha.hv-voice",
        "hvp"
    );
    /** The {@code application/vnd-yamaha-openscoreformat} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_OPENSCOREFORMAT =
        create("APPLICATION/VND.YAMAHA.OPENSCOREFORMAT", "OSF");
    /** The {@code application/vnd-yamaha-openscoreformat-osfpvg-xml} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_OPENSCOREFORMAT_OSFPVG_XML =
        create("APPLICATION/VND.YAMAHA.OPENSCOREFORMAT.OSFPVG+XML", "OSFPVG");
    /** The {@code application/vnd-yamaha-smaf-audio} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_SMAF_AUDIO = create(
        "application/vnd.yamaha.smaf-audio",
        "saf"
    );
    /** The {@code application/vnd-yamaha-smaf-phrase} media type. */
    public static final MediaType APPLICATION_VND_YAMAHA_SMAF_PHRASE = create(
        "application/vnd.yamaha.smaf-phrase",
        "spf"
    );
    /** The {@code application/vnd-yellowriver-custom-menu} media type. */
    public static final MediaType APPLICATION_VND_YELLOWRIVER_CUSTOM_MENU =
        create("APPLICATION/VND.YELLOWRIVER-CUSTOM-MENU", "CMP");
    /** The {@code application/vnd-zul} media type. */
    public static final MediaType APPLICATION_VND_ZUL = create(
        "application/vnd.zul",
        "zir",
        "zirz"
    );
    /** The {@code application/vnd-zzazz-deck-xml} media type. */
    public static final MediaType APPLICATION_VND_ZZAZZ_DECK_XML = create(
        "application/vnd.zzazz.deck+xml",
        "zaz"
    );
    /** The {@code application/voicexml-xml} media type. */
    public static final MediaType APPLICATION_VOICEXML_XML = create(
        "application/voicexml+xml",
        "vxml"
    );
    /** The {@code application/widget} media type. */
    public static final MediaType APPLICATION_WIDGET = create(
        "application/widget",
        "wgt"
    );
    /** The {@code application/winhlp} media type. */
    public static final MediaType APPLICATION_WINHLP = create(
        "application/winhlp",
        "hlp"
    );
    /** The {@code application/wsdl-xml} media type. */
    public static final MediaType APPLICATION_WSDL_XML = create(
        "application/wsdl+xml",
        "wsdl"
    );
    /** The {@code application/wspolicy-xml} media type. */
    public static final MediaType APPLICATION_WSPOLICY_XML = create(
        "application/wspolicy+xml",
        "wspolicy"
    );
    /** The {@code application/x-123} media type. */
    public static final MediaType APPLICATION_X_123 = create(
        "application/x-123",
        "wk"
    );
    /** The {@code application/x-7z-compressed} media type. */
    public static final MediaType APPLICATION_X_7Z_COMPRESSED = create(
        "application/x-7z-compressed",
        "7z"
    );
    /** The {@code application/x-abiword} media type. */
    public static final MediaType APPLICATION_X_ABIWORD = create(
        "application/x-abiword",
        "abw"
    );
    /** The {@code application/x-ace-compressed} media type. */
    public static final MediaType APPLICATION_X_ACE_COMPRESSED = create(
        "application/x-ace-compressed",
        "ace"
    );
    /** The {@code application/xaml-xml} media type. */
    public static final MediaType APPLICATION_XAML_XML = create(
        "application/xaml+xml",
        "xaml"
    );
    /** The {@code application/x-apple-diskimage} media type. */
    public static final MediaType APPLICATION_X_APPLE_DISKIMAGE = create(
        "application/x-apple-diskimage",
        "dmg"
    );
    /** The {@code application/x-authorware-bin} media type. */
    public static final MediaType APPLICATION_X_AUTHORWARE_BIN = create(
        "application/x-authorware-bin",
        "aab",
        "x32",
        "U32",
        "VOX"
    );
    /** The {@code application/x-authorware-map} media type. */
    public static final MediaType APPLICATION_X_AUTHORWARE_MAP = create(
        "application/x-authorware-map",
        "aam"
    );
    /** The {@code application/x-authorware-seg} media type. */
    public static final MediaType APPLICATION_X_AUTHORWARE_SEG = create(
        "application/x-authorware-seg",
        "aas"
    );
    /** The {@code application/x-bcpio} media type. */
    public static final MediaType APPLICATION_X_BCPIO = create(
        "application/x-bcpio",
        "bcpio"
    );
    /** The {@code application/x-bittorrent} media type. */
    public static final MediaType APPLICATION_X_BITTORRENT = create(
        "application/x-bittorrent",
        "torrent"
    );
    /** The {@code application/x-blorb} media type. */
    public static final MediaType APPLICATION_X_BLORB = create(
        "application/x-blorb",
        "blb",
        "blorb"
    );
    /** The {@code application/x-bzip2} media type. */
    public static final MediaType APPLICATION_X_BZIP2 = create(
        "application/x-bzip2",
        "bz2",
        "boz"
    );
    /** The {@code application/x-bzip} media type. */
    public static final MediaType APPLICATION_X_BZIP = create(
        "application/x-bzip",
        "bz"
    );
    /** The {@code application/x-cab} media type. */
    public static final MediaType APPLICATION_X_CAB = create(
        "application/x-cab",
        "cab"
    );
    /** The {@code application/xcap-diff-xml} media type. */
    public static final MediaType APPLICATION_XCAP_DIFF_XML = create(
        "application/xcap-diff+xml",
        "xdf"
    );
    /** The {@code application/x-cbr} media type. */
    public static final MediaType APPLICATION_X_CBR = create(
        "application/x-cbr",
        "cbr",
        "cba",
        "cbt",
        "cbz",
        "cb7"
    );
    /** The {@code application/x-cbz} media type. */
    public static final MediaType APPLICATION_X_CBZ = create(
        "application/x-cbz",
        "cbz"
    );
    /** The {@code application/x-cdf} media type. */
    public static final MediaType APPLICATION_X_CDF = create(
        "application/x-cdf",
        "cdf",
        "cda"
    );
    /** The {@code application/x-cdlink} media type. */
    public static final MediaType APPLICATION_X_CDLINK = create(
        "application/x-cdlink",
        "vcd"
    );
    /** The {@code application/x-cfs-compressed} media type. */
    public static final MediaType APPLICATION_X_CFS_COMPRESSED = create(
        "application/x-cfs-compressed",
        "cfs"
    );
    /** The {@code application/x-chat} media type. */
    public static final MediaType APPLICATION_X_CHAT = create(
        "application/x-chat",
        "chat"
    );
    /** The {@code application/x-chess-pgn} media type. */
    public static final MediaType APPLICATION_X_CHESS_PGN = create(
        "application/x-chess-pgn",
        "pgn"
    );
    /** The {@code application/x-comsol} media type. */
    public static final MediaType APPLICATION_X_COMSOL = create(
        "application/x-comsol",
        "mph"
    );
    /** The {@code application/x-conference} media type. */
    public static final MediaType APPLICATION_X_CONFERENCE = create(
        "application/x-conference",
        "nsc"
    );
    /** The {@code application/x-cpio} media type. */
    public static final MediaType APPLICATION_X_CPIO = create(
        "application/x-cpio",
        "cpio"
    );
    /** The {@code application/x-csh} media type. */
    public static final MediaType APPLICATION_X_CSH = create(
        "application/x-csh",
        "csh"
    );
    /** The {@code application/x-debian-package} media type. */
    public static final MediaType APPLICATION_X_DEBIAN_PACKAGE = create(
        "application/x-debian-package",
        "deb",
        "udeb"
    );
    /** The {@code application/x-dgc-compressed} media type. */
    public static final MediaType APPLICATION_X_DGC_COMPRESSED = create(
        "application/x-dgc-compressed",
        "dgc"
    );
    /** The {@code application/x-director} media type. */
    public static final MediaType APPLICATION_X_DIRECTOR = create(
        "application/x-director",
        "dir",
        "dcr",
        "dxr",
        "cst",
        "CCT",
        "CXT",
        "W3D",
        "FGD",
        "SWA"
    );
    /** The {@code application/x-dms} media type. */
    public static final MediaType APPLICATION_X_DMS = create(
        "application/x-dms",
        "dms"
    );
    /** The {@code application/x-doom} media type. */
    public static final MediaType APPLICATION_X_DOOM = create(
        "application/x-doom",
        "wad"
    );
    /** The {@code application/x-dtbncx-xml} media type. */
    public static final MediaType APPLICATION_X_DTBNCX_XML = create(
        "application/x-dtbncx+xml",
        "ncx"
    );
    /** The {@code application/x-dtbook-xml} media type. */
    public static final MediaType APPLICATION_X_DTBOOK_XML = create(
        "application/x-dtbook+xml",
        "dtb"
    );
    /** The {@code application/x-dtbresource-xml} media type. */
    public static final MediaType APPLICATION_X_DTBRESOURCE_XML = create(
        "application/x-dtbresource+xml",
        "res"
    );
    /** The {@code application/x-dvi} media type. */
    public static final MediaType APPLICATION_X_DVI = create(
        "application/x-dvi",
        "dvi"
    );
    /** The {@code application/xenc-xml} media type. */
    public static final MediaType APPLICATION_XENC_XML = create(
        "application/xenc+xml",
        "xenc"
    );
    /** The {@code application/x-envoy} media type. */
    public static final MediaType APPLICATION_X_ENVOY = create(
        "application/x-envoy",
        "evy"
    );
    /** The {@code application/x-eva} media type. */
    public static final MediaType APPLICATION_X_EVA = create(
        "application/x-eva",
        "eva"
    );
    /** The {@code application/x-font-bdf} media type. */
    public static final MediaType APPLICATION_X_FONT_BDF = create(
        "application/x-font-bdf",
        "bdf"
    );
    /** The {@code application/x-font-ghostscript} media type. */
    public static final MediaType APPLICATION_X_FONT_GHOSTSCRIPT = create(
        "application/x-font-ghostscript",
        "gsf"
    );
    /** The {@code application/x-font-linux-psf} media type. */
    public static final MediaType APPLICATION_X_FONT_LINUX_PSF = create(
        "application/x-font-linux-psf",
        "psf"
    );
    /** The {@code application/x-font-otf} media type. */
    public static final MediaType APPLICATION_X_FONT_OTF = create(
        "application/x-font-otf",
        "otf"
    );
    /** The {@code application/x-font-pcf} media type. */
    public static final MediaType APPLICATION_X_FONT_PCF = create(
        "application/x-font-pcf",
        "pcf"
    );
    /** The {@code application/x-font} media type. */
    public static final MediaType APPLICATION_X_FONT = create(
        "application/x-font",
        "pfa",
        "pfb",
        "gsf",
        "pcf",
        "pcf.z"
    );
    /** The {@code application/x-font-snf} media type. */
    public static final MediaType APPLICATION_X_FONT_SNF = create(
        "application/x-font-snf",
        "snf"
    );
    /** The {@code application/x-font-ttf} media type. */
    public static final MediaType APPLICATION_X_FONT_TTF = create(
        "application/x-font-ttf",
        "ttf",
        "ttc"
    );
    /** The {@code application/x-font-type1} media type. */
    public static final MediaType APPLICATION_X_FONT_TYPE1 = create(
        "application/x-font-type1",
        "pfa",
        "pfb",
        "pfm",
        "afm"
    );
    /** The {@code application/x-font-woff} media type. */
    public static final MediaType APPLICATION_X_FONT_WOFF = create(
        "application/x-font-woff",
        "woff",
        "woff2"
    );
    /** The {@code application/x-freearc} media type. */
    public static final MediaType APPLICATION_X_FREEARC = create(
        "application/x-freearc",
        "arc"
    );
    /** The {@code application/x-freemind} media type. */
    public static final MediaType APPLICATION_X_FREEMIND = create(
        "application/x-freemind",
        "mm"
    );
    /** The {@code application/x-futuresplash} media type. */
    public static final MediaType APPLICATION_X_FUTURESPLASH = create(
        "application/x-futuresplash",
        "spl"
    );
    /** The {@code application/x-ganttproject} media type. */
    public static final MediaType APPLICATION_X_GANTTPROJECT = create(
        "application/x-ganttproject",
        "gan"
    );
    /** The {@code application/x-gca-compressed} media type. */
    public static final MediaType APPLICATION_X_GCA_COMPRESSED = create(
        "application/x-gca-compressed",
        "gca"
    );
    /** The {@code application/x-glulx} media type. */
    public static final MediaType APPLICATION_X_GLULX = create(
        "application/x-glulx",
        "ulx"
    );
    /** The {@code application/x-gnumeric} media type. */
    public static final MediaType APPLICATION_X_GNUMERIC = create(
        "application/x-gnumeric",
        "gnumeric"
    );
    /** The {@code application/x-go-sgf} media type. */
    public static final MediaType APPLICATION_X_GO_SGF = create(
        "application/x-go-sgf",
        "sgf"
    );
    /** The {@code application/x-gramps-xml} media type. */
    public static final MediaType APPLICATION_X_GRAMPS_XML = create(
        "application/x-gramps-xml",
        "gramps"
    );
    /** The {@code application/x-graphing-calculator} media type. */
    public static final MediaType APPLICATION_X_GRAPHING_CALCULATOR = create(
        "application/x-graphing-calculator",
        "gcf"
    );
    /** The {@code application/x-gtar-compressed} media type. */
    public static final MediaType APPLICATION_X_GTAR_COMPRESSED = create(
        "application/x-gtar-compressed",
        "tgz",
        "taz"
    );
    /** The {@code application/x-gzip-compressed} media type. */
    public static final MediaType APPLICATION_X_GZIP_COMPRESSED = create(
        "application/x-gzip",
        "gz"
    );
    /** The {@code application/x-gtar} media type. */
    public static final MediaType APPLICATION_X_GTAR = create(
        "application/x-gtar",
        "gtar"
    );
    /** The {@code application/x-hdf} media type. */
    public static final MediaType APPLICATION_X_HDF = create(
        "application/x-hdf",
        "hdf"
    );
    /** The {@code application/xhtml-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_XHTML_XML_UTF8 = createUTF8(
        "application/xhtml+xml",
        "xhtml",
        "xht"
    );
    /** The {@code application/x-httpd-eruby} media type. */
    public static final MediaType APPLICATION_X_HTTPD_ERUBY = create(
        "application/x-httpd-eruby",
        "rhtml"
    );
    /** The {@code application/x-httpd-php3} media type. */
    public static final MediaType APPLICATION_X_HTTPD_PHP3 = create(
        "application/x-httpd-php3",
        "php3"
    );
    /** The {@code application/x-httpd-php3-preprocessed} media type. */
    public static final MediaType APPLICATION_X_HTTPD_PHP3_PREPROCESSED =
        create("application/x-httpd-php3-preprocessed", "PHP3P");
    /** The {@code application/x-httpd-php4} media type. */
    public static final MediaType APPLICATION_X_HTTPD_PHP4 = create(
        "application/x-httpd-php4",
        "php4"
    );
    /** The {@code application/x-httpd-php5} media type. */
    public static final MediaType APPLICATION_X_HTTPD_PHP5 = create(
        "application/x-httpd-php5",
        "php5"
    );
    /** The {@code application/x-httpd-php} media type. */
    public static final MediaType APPLICATION_X_HTTPD_PHP = create(
        "application/x-httpd-php",
        "phtml",
        "pht",
        "php"
    );
    /** The {@code application/x-httpd-php-source} media type. */
    public static final MediaType APPLICATION_X_HTTPD_PHP_SOURCE = create(
        "application/x-httpd-php-source",
        "phps"
    );
    /** The {@code application/x-ica} media type. */
    public static final MediaType APPLICATION_X_ICA = create(
        "application/x-ica",
        "ica"
    );
    /** The {@code application/x-info} media type. */
    public static final MediaType APPLICATION_X_INFO = create(
        "application/x-info",
        "info"
    );
    /** The {@code application/x-install-instructions} media type. */
    public static final MediaType APPLICATION_X_INSTALL_INSTRUCTIONS = create(
        "application/x-install-instructions",
        "INSTALL"
    );
    /** The {@code application/x-internet-signup} media type. */
    public static final MediaType APPLICATION_X_INTERNET_SIGNUP = create(
        "application/x-internet-signup",
        "ins",
        "isp"
    );
    /** The {@code application/x-iphone} media type. */
    public static final MediaType APPLICATION_X_IPHONE = create(
        "application/x-iphone",
        "iii"
    );
    /** The {@code application/x-iso9660-image} media type. */
    public static final MediaType APPLICATION_X_ISO9660_IMAGE = create(
        "application/x-iso9660-image",
        "iso"
    );
    /** The {@code application/x-jam} media type. */
    public static final MediaType APPLICATION_X_JAM = create(
        "application/x-jam",
        "jam"
    );
    /** The {@code application/x-java-jnlp-file} media type. */
    public static final MediaType APPLICATION_X_JAVA_JNLP_FILE = create(
        "application/x-java-jnlp-file",
        "jnlp"
    );
    /** The {@code application/x-jmol} media type. */
    public static final MediaType APPLICATION_X_JMOL = create(
        "application/x-jmol",
        "jmz"
    );
    /** The {@code application/x-kchart} media type. */
    public static final MediaType APPLICATION_X_KCHART = create(
        "application/x-kchart",
        "chrt"
    );
    /** The {@code application/x-killustrator} media type. */
    public static final MediaType APPLICATION_X_KILLUSTRATOR = create(
        "application/x-killustrator",
        "kil"
    );
    /** The {@code application/x-koan} media type. */
    public static final MediaType APPLICATION_X_KOAN = create(
        "application/x-koan",
        "skp",
        "skd",
        "skt",
        "skm"
    );
    /** The {@code application/x-kpresenter} media type. */
    public static final MediaType APPLICATION_X_KPRESENTER = create(
        "application/x-kpresenter",
        "kpr",
        "kpt"
    );
    /** The {@code application/x-kspread} media type. */
    public static final MediaType APPLICATION_X_KSPREAD = create(
        "application/x-kspread",
        "ksp"
    );
    /** The {@code application/x-kword} media type. */
    public static final MediaType APPLICATION_X_KWORD = create(
        "application/x-kword",
        "kwd",
        "kwt"
    );
    /** The {@code application/x-latex} media type. */
    public static final MediaType APPLICATION_X_LATEX = create(
        "application/x-latex",
        "latex"
    );
    /** The {@code application/x-lha} media type. */
    public static final MediaType APPLICATION_X_LHA = create(
        "application/x-lha",
        "lha"
    );
    /** The {@code application/x-lyx} media type. */
    public static final MediaType APPLICATION_X_LYX = create(
        "application/x-lyx",
        "lyx"
    );
    /** The {@code application/x-lzh-compressed} media type. */
    public static final MediaType APPLICATION_X_LZH_COMPRESSED = create(
        "application/x-lzh-compressed",
        "lzh",
        "lha"
    );
    /** The {@code application/x-lzh} media type. */
    public static final MediaType APPLICATION_X_LZH = create(
        "application/x-lzh",
        "lzh"
    );
    /** The {@code application/x-lzx} media type. */
    public static final MediaType APPLICATION_X_LZX = create(
        "application/x-lzx",
        "lzx"
    );
    /** The {@code application/x-maker} media type. */
    public static final MediaType APPLICATION_X_MAKER = create(
        "application/x-maker",
        "frm",
        "maker",
        "frame",
        "fm",
        "fb",
        "BOOK",
        "FBDOC"
    );
    /** The {@code application/x-mie} media type. */
    public static final MediaType APPLICATION_X_MIE = create(
        "application/x-mie",
        "mie"
    );
    /** The {@code application/x-mif} media type. */
    public static final MediaType APPLICATION_X_MIF = create(
        "application/x-mif",
        "mif"
    );
    /** The {@code application/xml-dtd with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_XML_DTD_UTF8 = createUTF8(
        "application/xml-dtd",
        "dtd"
    );
    /** The {@code application/xml} media type. */
    public static final MediaType APPLICATION_XML = create(
        "application/xml",
        "xml",
        "xsl",
        "xsd"
    );
    /** The {@code application/xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_XML_UTF8 = createUTF8(
        "application/xml",
        "xml",
        "xsl",
        "xsd"
    );
    /** The {@code application/x-mobipocket-ebook} media type. */
    public static final MediaType APPLICATION_X_MOBIPOCKET_EBOOK = create(
        "application/x-mobipocket-ebook",
        "prc",
        "mobi"
    );
    /** The {@code application/x-mpegurl} media type. */
    public static final MediaType APPLICATION_X_MPEGURL = create(
        "application/x-mpegurl",
        "m3u8"
    );
    /** The {@code application/x-msaccess} media type. */
    public static final MediaType APPLICATION_X_MSACCESS = create(
        "application/x-msaccess",
        "mdb"
    );
    /** The {@code application/x-ms-application} media type. */
    public static final MediaType APPLICATION_X_MS_APPLICATION = create(
        "application/x-ms-application",
        "application"
    );
    /** The {@code application/x-msbinder} media type. */
    public static final MediaType APPLICATION_X_MSBINDER = create(
        "application/x-msbinder",
        "obd"
    );
    /** The {@code application/x-mscardfile} media type. */
    public static final MediaType APPLICATION_X_MSCARDFILE = create(
        "application/x-mscardfile",
        "crd"
    );
    /** The {@code application/x-msclip} media type. */
    public static final MediaType APPLICATION_X_MSCLIP = create(
        "application/x-msclip",
        "clp"
    );
    /** The {@code application/x-msdos-program} media type. */
    public static final MediaType APPLICATION_X_MSDOS_PROGRAM = create(
        "application/x-msdos-program",
        "com",
        "exe",
        "bat",
        "DLL"
    );
    /** The {@code application/x-msdownload} media type. */
    public static final MediaType APPLICATION_X_MSDOWNLOAD = create(
        "application/x-msdownload",
        "exe",
        "dll",
        "com",
        "BAT",
        "MSI"
    );
    /** The {@code application/x-msi} media type. */
    public static final MediaType APPLICATION_X_MSI = create(
        "application/x-msi",
        "msi"
    );
    /** The {@code application/x-msmediaview} media type. */
    public static final MediaType APPLICATION_X_MSMEDIAVIEW = create(
        "application/x-msmediaview",
        "mvb",
        "m13",
        "m14"
    );
    /** The {@code application/x-msmetafile} media type. */
    public static final MediaType APPLICATION_X_MSMETAFILE = create(
        "application/x-msmetafile",
        "wmf",
        "wmz",
        "emf",
        "emz"
    );
    /** The {@code application/x-msmoney} media type. */
    public static final MediaType APPLICATION_X_MSMONEY = create(
        "application/x-msmoney",
        "mny"
    );
    /** The {@code application/x-mspublisher} media type. */
    public static final MediaType APPLICATION_X_MSPUBLISHER = create(
        "application/x-mspublisher",
        "pub"
    );
    /** The {@code application/x-msschedule} media type. */
    public static final MediaType APPLICATION_X_MSSCHEDULE = create(
        "application/x-msschedule",
        "scd"
    );
    /** The {@code application/x-ms-shortcut} media type. */
    public static final MediaType APPLICATION_X_MS_SHORTCUT = create(
        "application/x-ms-shortcut",
        "lnk"
    );
    /** The {@code application/x-msterminal} media type. */
    public static final MediaType APPLICATION_X_MSTERMINAL = create(
        "application/x-msterminal",
        "trm"
    );
    /** The {@code application/x-ms-wmd} media type. */
    public static final MediaType APPLICATION_X_MS_WMD = create(
        "application/x-ms-wmd",
        "wmd"
    );
    /** The {@code application/x-ms-wmz} media type. */
    public static final MediaType APPLICATION_X_MS_WMZ = create(
        "application/x-ms-wmz",
        "wmz"
    );
    /** The {@code application/x-mswrite} media type. */
    public static final MediaType APPLICATION_X_MSWRITE = create(
        "application/x-mswrite",
        "wri"
    );
    /** The {@code application/x-ms-xbap} media type. */
    public static final MediaType APPLICATION_X_MS_XBAP = create(
        "application/x-ms-xbap",
        "xbap"
    );
    /** The {@code application/x-netcdf} media type. */
    public static final MediaType APPLICATION_X_NETCDF = create(
        "application/x-netcdf",
        "nc",
        "cdf"
    );
    /** The {@code application/x-ns-proxy-autoconfig} media type. */
    public static final MediaType APPLICATION_X_NS_PROXY_AUTOCONFIG = create(
        "application/x-ns-proxy-autoconfig",
        "pac",
        "DAT"
    );
    /** The {@code application/x-nwc} media type. */
    public static final MediaType APPLICATION_X_NWC = create(
        "application/x-nwc",
        "nwc"
    );
    /** The {@code application/x-nzb} media type. */
    public static final MediaType APPLICATION_X_NZB = create(
        "application/x-nzb",
        "nzb"
    );
    /** The {@code application/x-object} media type. */
    public static final MediaType APPLICATION_X_OBJECT = create(
        "application/x-object",
        "o"
    );
    /** The {@code application/xop-xml} media type. */
    public static final MediaType APPLICATION_XOP_XML = create(
        "application/xop+xml",
        "xop"
    );
    /** The {@code application/x-oz-application} media type. */
    public static final MediaType APPLICATION_X_OZ_APPLICATION = create(
        "application/x-oz-application",
        "oza"
    );
    /** The {@code application/x-pkcs12} media type. */
    public static final MediaType APPLICATION_X_PKCS12 = create(
        "application/x-pkcs12",
        "p12",
        "pfx"
    );
    /** The {@code application/x-pkcs7-certificates} media type. */
    public static final MediaType APPLICATION_X_PKCS7_CERTIFICATES = create(
        "application/x-pkcs7-certificates",
        "p7b",
        "SPC"
    );
    /** The {@code application/x-pkcs7-certreqresp} media type. */
    public static final MediaType APPLICATION_X_PKCS7_CERTREQRESP = create(
        "application/x-pkcs7-certreqresp",
        "p7r"
    );
    /** The {@code application/x-pkcs7-crl} media type. */
    public static final MediaType APPLICATION_X_PKCS7_CRL = create(
        "application/x-pkcs7-crl",
        "crl"
    );
    /** The {@code application/xproc-xml} media type. */
    public static final MediaType APPLICATION_XPROC_XML = create(
        "application/xproc+xml",
        "xpl"
    );
    /** The {@code application/x-python-code} media type. */
    public static final MediaType APPLICATION_X_PYTHON_CODE = create(
        "application/x-python-code",
        "pyc",
        "pyo"
    );
    /** The {@code application/x-qgis} media type. */
    public static final MediaType APPLICATION_X_QGIS = create(
        "application/x-qgis",
        "qgs",
        "shp",
        "shx"
    );
    /** The {@code application/x-quicktimeplayer} media type. */
    public static final MediaType APPLICATION_X_QUICKTIMEPLAYER = create(
        "application/x-quicktimeplayer",
        "qtl"
    );
    /** The {@code application/x-rar-compressed} media type. */
    public static final MediaType APPLICATION_X_RAR_COMPRESSED = create(
        "application/x-rar-compressed",
        "rar"
    );
    /** The {@code application/x-rdp} media type. */
    public static final MediaType APPLICATION_X_RDP = create(
        "application/x-rdp",
        "rdp"
    );
    /** The {@code application/x-redhat-package-manager} media type. */
    public static final MediaType APPLICATION_X_REDHAT_PACKAGE_MANAGER = create(
        "application/x-redhat-package-manager",
        "RPM"
    );
    /** The {@code application/x-research-info-systems} media type. */
    public static final MediaType APPLICATION_X_RESEARCH_INFO_SYSTEMS = create(
        "application/x-research-info-systems",
        "RIS"
    );
    /** The {@code application/x-ruby} media type. */
    public static final MediaType APPLICATION_X_RUBY = create(
        "application/x-ruby",
        "rb"
    );
    /** The {@code application/x-www-form-urlencoded} media type. */
    public static final MediaType APPLICATION_X_WWW_FORM_URLENCODED = create(
        "application/x-www-form-urlencoded"
    );

    /** The {@code application/x-scilab} media type. */
    public static final MediaType APPLICATION_X_SCILAB = create(
        "application/x-scilab",
        "sci",
        "sce"
    );
    /** The {@code application/x-shar} media type. */
    public static final MediaType APPLICATION_X_SHAR = create(
        "application/x-shar",
        "shar"
    );
    /** The {@code application/x-shockwave-flash} media type. */
    public static final MediaType APPLICATION_X_SHOCKWAVE_FLASH = create(
        "application/x-shockwave-flash",
        "swf",
        "swfl"
    );
    /** The {@code application/x-sh with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_X_SH_UTF8 = createUTF8(
        "application/x-sh",
        "sh"
    );
    /** The {@code application/x-silverlight-app} media type. */
    public static final MediaType APPLICATION_X_SILVERLIGHT_APP = create(
        "application/x-silverlight-app",
        "xap"
    );
    /** The {@code application/x-silverlight} media type. */
    public static final MediaType APPLICATION_X_SILVERLIGHT = create(
        "application/x-silverlight",
        "scr"
    );
    /** The {@code application/xslt-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_XSLT_XML_UTF8 = createUTF8(
        "application/xslt+xml",
        "xslt"
    );
    /** The {@code application/xspf-xml with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_XSPF_XML_UTF8 = createUTF8(
        "application/xspf+xml",
        "xspf"
    );
    /** The {@code application/x-sql with UTF-8 charset} media type. */
    public static final MediaType APPLICATION_X_SQL_UTF8 = createUTF8(
        "application/x-sql",
        "sql"
    );
    /** The {@code application/x-stuffit} media type. */
    public static final MediaType APPLICATION_X_STUFFIT = create(
        "application/x-stuffit",
        "sit",
        "sitx"
    );
    /** The {@code application/x-stuffitx} media type. */
    public static final MediaType APPLICATION_X_STUFFITX = create(
        "application/x-stuffitx",
        "sitx"
    );
    /** The {@code application/x-subrip} media type. */
    public static final MediaType APPLICATION_X_SUBRIP = create(
        "application/x-subrip",
        "srt"
    );
    /** The {@code application/x-sv4cpio} media type. */
    public static final MediaType APPLICATION_X_SV4CPIO = create(
        "application/x-sv4cpio",
        "sv4cpio"
    );
    /** The {@code application/x-sv4crc} media type. */
    public static final MediaType APPLICATION_X_SV4CRC = create(
        "application/x-sv4crc",
        "sv4crc"
    );
    /** The {@code application/x-t3vm-image} media type. */
    public static final MediaType APPLICATION_X_T3VM_IMAGE = create(
        "application/x-t3vm-image",
        "t3"
    );
    /** The {@code application/x-tads} media type. */
    public static final MediaType APPLICATION_X_TADS = create(
        "application/x-tads",
        "gam"
    );
    /** The {@code application/x-tar} media type. */
    public static final MediaType APPLICATION_X_TAR = create(
        "application/x-tar",
        "tar"
    );
    /** The {@code application/x-tcl} media type. */
    public static final MediaType APPLICATION_X_TCL = create(
        "application/x-tcl",
        "tcl"
    );
    /** The {@code application/x-tex-gf} media type. */
    public static final MediaType APPLICATION_X_TEX_GF = create(
        "application/x-tex-gf",
        "gf"
    );
    /** The {@code application/x-texinfo} media type. */
    public static final MediaType APPLICATION_X_TEXINFO = create(
        "application/x-texinfo",
        "texinfo",
        "texi"
    );
    /** The {@code application/x-tex-pk} media type. */
    public static final MediaType APPLICATION_X_TEX_PK = create(
        "application/x-tex-pk",
        "pk"
    );
    /** The {@code application/x-tex} media type. */
    public static final MediaType APPLICATION_X_TEX = create(
        "application/x-tex",
        "tex"
    );
    /** The {@code application/x-tex-tfm} media type. */
    public static final MediaType APPLICATION_X_TEX_TFM = create(
        "application/x-tex-tfm",
        "tfm"
    );
    /** The {@code application/x-tgif} media type. */
    public static final MediaType APPLICATION_X_TGIF = create(
        "application/x-tgif",
        "obj"
    );
    /** The {@code application/x-trash} media type. */
    public static final MediaType APPLICATION_X_TRASH = create(
        "application/x-trash",
        "~",
        "%",
        "bak",
        "old",
        "sik"
    );
    /** The {@code application/x-troff-man} media type. */
    public static final MediaType APPLICATION_X_TROFF_MAN = create(
        "application/x-troff-man",
        "man"
    );
    /** The {@code application/x-troff-me} media type. */
    public static final MediaType APPLICATION_X_TROFF_ME = create(
        "application/x-troff-me",
        "me"
    );
    /** The {@code application/x-troff-ms} media type. */
    public static final MediaType APPLICATION_X_TROFF_MS = create(
        "application/x-troff-ms",
        "ms"
    );
    /** The {@code application/x-troff} media type. */
    public static final MediaType APPLICATION_X_TROFF = create(
        "application/x-troff",
        "t",
        "tr",
        "roff"
    );
    /** The {@code application/x-ustar} media type. */
    public static final MediaType APPLICATION_X_USTAR = create(
        "application/x-ustar",
        "ustar"
    );
    /** The {@code application/xv-xml} media type. */
    public static final MediaType APPLICATION_XV_XML = create(
        "application/xv+xml",
        "mxml",
        "xhvml",
        "xvml",
        "xvm"
    );
    /** The {@code application/x-wais-source} media type. */
    public static final MediaType APPLICATION_X_WAIS_SOURCE = create(
        "application/x-wais-source",
        "src"
    );
    /** The {@code application/x-wingz} media type. */
    public static final MediaType APPLICATION_X_WINGZ = create(
        "application/x-wingz",
        "wz"
    );
    /** The {@code application/x-x509-ca-cert} media type. */
    public static final MediaType APPLICATION_X_X509_CA_CERT = create(
        "application/x-x509-ca-cert",
        "der",
        "crt"
    );
    /** The {@code application/x-xcf} media type. */
    public static final MediaType APPLICATION_X_XCF = create(
        "application/x-xcf",
        "xcf"
    );
    /** The {@code application/x-xfig} media type. */
    public static final MediaType APPLICATION_X_XFIG = create(
        "application/x-xfig",
        "fig"
    );
    /** The {@code application/x-xliff-xml} media type. */
    public static final MediaType APPLICATION_X_XLIFF_XML = create(
        "application/x-xliff+xml",
        "xlf"
    );
    /** The {@code application/x-xpinstall} media type. */
    public static final MediaType APPLICATION_X_XPINSTALL = create(
        "application/x-xpinstall",
        "xpi"
    );
    /** The {@code application/x-xz} media type. */
    public static final MediaType APPLICATION_X_XZ = create(
        "application/x-xz",
        "xz"
    );
    /** The {@code application/x-zmachine} media type. */
    public static final MediaType APPLICATION_X_ZMACHINE = create(
        "application/x-zmachine",
        "z1",
        "z2",
        "z3",
        "z4",
        "z5",
        "Z6",
        "Z7",
        "Z8"
    );
    /** The {@code application/yang} media type. */
    public static final MediaType APPLICATION_YANG = create(
        "application/yang",
        "yang"
    );
    /** The {@code application/yin-xml} media type. */
    public static final MediaType APPLICATION_YIN_XML = create(
        "application/yin+xml",
        "yin"
    );
    /** The {@code application/zip} media type. */
    public static final MediaType APPLICATION_ZIP = create(
        "application/zip",
        "zip"
    );
    /** The {@code audio/adpcm} media type. */
    public static final MediaType AUDIO_ADPCM = create("audio/adpcm", "adp");
    /** The {@code audio/amr} media type. */
    public static final MediaType AUDIO_AMR = create("audio/amr", "amr");
    /** The {@code audio/amr-wb} media type. */
    public static final MediaType AUDIO_AMR_WB = create("audio/amr-wb", "awb");
    /** The {@code audio/annodex} media type. */
    public static final MediaType AUDIO_ANNODEX = create(
        "audio/annodex",
        "axa"
    );
    /** The {@code audio/basic} media type. */
    public static final MediaType AUDIO_BASIC = create(
        "audio/basic",
        "au",
        "snd"
    );
    /** The {@code audio/csound} media type. */
    public static final MediaType AUDIO_CSOUND = create(
        "audio/csound",
        "csd",
        "orc",
        "sco"
    );
    /** The {@code audio/flac} media type. */
    public static final MediaType AUDIO_FLAC = create("audio/flac", "flac");
    /** The {@code audio/midi} media type. */
    public static final MediaType AUDIO_MIDI = create(
        "audio/midi",
        "mid",
        "midi",
        "kar",
        "rmi"
    );
    /** The {@code audio/mp4} media type. */
    public static final MediaType AUDIO_MP4 = create("audio/mp4", "mp4a");
    /** The {@code audio/mpeg} media type. */
    public static final MediaType AUDIO_MPEG = create(
        "audio/mpeg",
        "mpga",
        "mpega",
        "mp2",
        "mp2a",
        "mp3",
        "m2a",
        "m3a",
        "MP3",
        "M4A"
    );
    /** The {@code audio/mpegurl} media type. */
    public static final MediaType AUDIO_MPEGURL = create(
        "audio/mpegurl",
        "m3u"
    );
    /** The {@code audio/ogg} media type. */
    public static final MediaType AUDIO_OGG = create(
        "audio/ogg",
        "oga",
        "ogg",
        "spx"
    );
    /** The {@code audio/prs-sid} media type. */
    public static final MediaType AUDIO_PRS_SID = create(
        "audio/prs.sid",
        "sid"
    );
    /** The {@code audio/s3m} media type. */
    public static final MediaType AUDIO_S3M = create("audio/s3m", "s3m");
    /** The {@code audio/silk} media type. */
    public static final MediaType AUDIO_SILK = create("audio/silk", "sil");
    /** The {@code audio/vnd-dece-audio} media type. */
    public static final MediaType AUDIO_VND_DECE_AUDIO = create(
        "audio/vnd.dece.audio",
        "uva",
        "uvva"
    );
    /** The {@code audio/vnd-digital-winds} media type. */
    public static final MediaType AUDIO_VND_DIGITAL_WINDS = create(
        "audio/vnd.digital-winds",
        "eol"
    );
    /** The {@code audio/vnd-dra} media type. */
    public static final MediaType AUDIO_VND_DRA = create(
        "audio/vnd.dra",
        "dra"
    );
    /** The {@code audio/vnd-dts} media type. */
    public static final MediaType AUDIO_VND_DTS = create(
        "audio/vnd.dts",
        "dts"
    );
    /** The {@code audio/vnd-dts-hd} media type. */
    public static final MediaType AUDIO_VND_DTS_HD = create(
        "audio/vnd.dts.hd",
        "dtshd"
    );
    /** The {@code audio/vnd-lucent-voice} media type. */
    public static final MediaType AUDIO_VND_LUCENT_VOICE = create(
        "audio/vnd.lucent.voice",
        "lvp"
    );
    /** The {@code audio/vnd-ms-playready-media-pya} media type. */
    public static final MediaType AUDIO_VND_MS_PLAYREADY_MEDIA_PYA = create(
        "audio/vnd.ms-playready.media.pya",
        "pya"
    );
    /** The {@code audio/vnd-nuera-ecelp4800} media type. */
    public static final MediaType AUDIO_VND_NUERA_ECELP4800 = create(
        "audio/vnd.nuera.ecelp4800",
        "ecelp4800"
    );
    /** The {@code audio/vnd-nuera-ecelp7470} media type. */
    public static final MediaType AUDIO_VND_NUERA_ECELP7470 = create(
        "audio/vnd.nuera.ecelp7470",
        "ecelp7470"
    );
    /** The {@code audio/vnd-nuera-ecelp9600} media type. */
    public static final MediaType AUDIO_VND_NUERA_ECELP9600 = create(
        "audio/vnd.nuera.ecelp9600",
        "ecelp9600"
    );
    /** The {@code audio/vnd-rip} media type. */
    public static final MediaType AUDIO_VND_RIP = create(
        "audio/vnd.rip",
        "rip"
    );
    /** The {@code audio/webm} media type. */
    public static final MediaType AUDIO_WEBM = create("audio/webm", "weba");
    /** The {@code audio/x-aac} media type. */
    public static final MediaType AUDIO_X_AAC = create("audio/x-aac", "aac");
    /** The {@code audio/x-aiff} media type. */
    public static final MediaType AUDIO_X_AIFF = create(
        "audio/x-aiff",
        "aif",
        "aiff",
        "aifc"
    );
    /** The {@code audio/x-caf} media type. */
    public static final MediaType AUDIO_X_CAF = create("audio/x-caf", "caf");
    /** The {@code audio/x-flac} media type. */
    public static final MediaType AUDIO_X_FLAC = create("audio/x-flac", "flac");
    /** The {@code audio/x-gsm} media type. */
    public static final MediaType AUDIO_X_GSM = create("audio/x-gsm", "gsm");
    /** The {@code audio/x-matroska} media type. */
    public static final MediaType AUDIO_X_MATROSKA = create(
        "audio/x-matroska",
        "mka"
    );
    /** The {@code audio/x-mpegurl} media type. */
    public static final MediaType AUDIO_X_MPEGURL = create(
        "audio/x-mpegurl",
        "m3u"
    );
    /** The {@code audio/x-ms-wax} media type. */
    public static final MediaType AUDIO_X_MS_WAX = create(
        "audio/x-ms-wax",
        "wax"
    );
    /** The {@code audio/x-ms-wma} media type. */
    public static final MediaType AUDIO_X_MS_WMA = create(
        "audio/x-ms-wma",
        "wma"
    );
    /** The {@code audio/xm} media type. */
    public static final MediaType AUDIO_XM = create("audio/xm", "xm");
    /** The {@code audio/x-pn-realaudio-plugin} media type. */
    public static final MediaType AUDIO_X_PN_REALAUDIO_PLUGIN = create(
        "audio/x-pn-realaudio-plugin",
        "rmp"
    );
    /** The {@code audio/x-pn-realaudio} media type. */
    public static final MediaType AUDIO_X_PN_REALAUDIO = create(
        "audio/x-pn-realaudio",
        "ra",
        "rm",
        "ram"
    );
    /** The {@code audio/x-realaudio} media type. */
    public static final MediaType AUDIO_X_REALAUDIO = create(
        "audio/x-realaudio",
        "ra"
    );
    /** The {@code audio/x-scpls} media type. */
    public static final MediaType AUDIO_X_SCPLS = create(
        "audio/x-scpls",
        "pls"
    );
    /** The {@code audio/x-sd2} media type. */
    public static final MediaType AUDIO_X_SD2 = create("audio/x-sd2", "sd2");
    /** The {@code audio/x-wav} media type. */
    public static final MediaType AUDIO_X_WAV = create("audio/x-wav", "wav");
    /** CHEMICAL_X_ALCHEMY media type constant. */
    public static final MediaType CHEMICAL_X_ALCHEMY = create(
        "chemical/x-alchemy",
        "alc"
    );
    /** CHEMICAL_X_CACHE media type constant. */
    public static final MediaType CHEMICAL_X_CACHE = create(
        "chemical/x-cache",
        "cac",
        "cache"
    );
    /** CHEMICAL_X_CACHE_CSF media type constant. */
    public static final MediaType CHEMICAL_X_CACHE_CSF = create(
        "chemical/x-cache-csf",
        "csf"
    );
    /** CHEMICAL_X_CACTVS_BINARY media type constant. */
    public static final MediaType CHEMICAL_X_CACTVS_BINARY = create(
        "chemical/x-cactvs-binary",
        "cbin",
        "cascii",
        "ctab"
    );
    /** CHEMICAL_X_CDX media type constant. */
    public static final MediaType CHEMICAL_X_CDX = create(
        "chemical/x-cdx",
        "cdx"
    );
    /** CHEMICAL_X_CERIUS media type constant. */
    public static final MediaType CHEMICAL_X_CERIUS = create(
        "chemical/x-cerius",
        "cer"
    );
    /** CHEMICAL_X_CHEM3D media type constant. */
    public static final MediaType CHEMICAL_X_CHEM3D = create(
        "chemical/x-chem3d",
        "c3d"
    );
    /** CHEMICAL_X_CHEMDRAW media type constant. */
    public static final MediaType CHEMICAL_X_CHEMDRAW = create(
        "chemical/x-chemdraw",
        "chm"
    );
    /** CHEMICAL_X_CIF media type constant. */
    public static final MediaType CHEMICAL_X_CIF = create(
        "chemical/x-cif",
        "cif"
    );
    /** CHEMICAL_X_CMDF media type constant. */
    public static final MediaType CHEMICAL_X_CMDF = create(
        "chemical/x-cmdf",
        "cmdf"
    );
    /** CHEMICAL_X_CML media type constant. */
    public static final MediaType CHEMICAL_X_CML = create(
        "chemical/x-cml",
        "cml"
    );
    /** CHEMICAL_X_COMPASS media type constant. */
    public static final MediaType CHEMICAL_X_COMPASS = create(
        "chemical/x-compass",
        "cpa"
    );
    /** CHEMICAL_X_CROSSFIRE media type constant. */
    public static final MediaType CHEMICAL_X_CROSSFIRE = create(
        "chemical/x-crossfire",
        "bsd"
    );
    /** CHEMICAL_X_CSML media type constant. */
    public static final MediaType CHEMICAL_X_CSML = create(
        "chemical/x-csml",
        "csml",
        "csm"
    );
    /** CHEMICAL_X_CTX media type constant. */
    public static final MediaType CHEMICAL_X_CTX = create(
        "chemical/x-ctx",
        "ctx"
    );
    /** CHEMICAL_X_CXF media type constant. */
    public static final MediaType CHEMICAL_X_CXF = create(
        "chemical/x-cxf",
        "cxf",
        "cef"
    );
    /** CHEMICAL_X_DAYLIGHT_SMILES media type constant. */
    public static final MediaType CHEMICAL_X_DAYLIGHT_SMILES = create(
        "chemical/x-daylight-smiles",
        "smi"
    );
    /** CHEMICAL_X_EMBL_DL_NUCLEOTIDE media type constant. */
    public static final MediaType CHEMICAL_X_EMBL_DL_NUCLEOTIDE = create(
        "chemical/x-embl-dl-nucleotide",
        "emb",
        "embl"
    );
    /** CHEMICAL_X_GALACTIC_SPC media type constant. */
    public static final MediaType CHEMICAL_X_GALACTIC_SPC = create(
        "chemical/x-galactic-spc",
        "spc"
    );
    /** CHEMICAL_X_GAMESS_INPUT media type constant. */
    public static final MediaType CHEMICAL_X_GAMESS_INPUT = create(
        "chemical/x-gamess-input",
        "inp",
        "gam",
        "gamin"
    );
    /** CHEMICAL_X_GAUSSIAN_CHECKPOINT media type constant. */
    public static final MediaType CHEMICAL_X_GAUSSIAN_CHECKPOINT = create(
        "chemical/x-gaussian-checkpoint",
        "fch",
        "fchk"
    );
    /** CHEMICAL_X_GAUSSIAN_CUBE media type constant. */
    public static final MediaType CHEMICAL_X_GAUSSIAN_CUBE = create(
        "chemical/x-gaussian-cube",
        "cub"
    );
    /** CHEMICAL_X_GAUSSIAN_INPUT media type constant. */
    public static final MediaType CHEMICAL_X_GAUSSIAN_INPUT = create(
        "chemical/x-gaussian-input",
        "gau",
        "gjc",
        "gjf"
    );
    /** CHEMICAL_X_GAUSSIAN_LOG media type constant. */
    public static final MediaType CHEMICAL_X_GAUSSIAN_LOG = create(
        "chemical/x-gaussian-log",
        "gal"
    );
    /** CHEMICAL_X_GCG8_SEQUENCE media type constant. */
    public static final MediaType CHEMICAL_X_GCG8_SEQUENCE = create(
        "chemical/x-gcg8-sequence",
        "gcg"
    );
    /** CHEMICAL_X_GENBANK media type constant. */
    public static final MediaType CHEMICAL_X_GENBANK = create(
        "chemical/x-genbank",
        "gen"
    );
    /** CHEMICAL_X_HIN media type constant. */
    public static final MediaType CHEMICAL_X_HIN = create(
        "chemical/x-hin",
        "hin"
    );
    /** CHEMICAL_X_ISOSTAR media type constant. */
    public static final MediaType CHEMICAL_X_ISOSTAR = create(
        "chemical/x-isostar",
        "istr",
        "ist"
    );
    /** CHEMICAL_X_JCAMP_DX media type constant. */
    public static final MediaType CHEMICAL_X_JCAMP_DX = create(
        "chemical/x-jcamp-dx",
        "jdx",
        "dx"
    );
    /** CHEMICAL_X_KINEMAGE media type constant. */
    public static final MediaType CHEMICAL_X_KINEMAGE = create(
        "chemical/x-kinemage",
        "kin"
    );
    /** CHEMICAL_X_MACMOLECULE media type constant. */
    public static final MediaType CHEMICAL_X_MACMOLECULE = create(
        "chemical/x-macmolecule",
        "mcm"
    );
    /** CHEMICAL_X_MACROMODEL_INPUT media type constant. */
    public static final MediaType CHEMICAL_X_MACROMODEL_INPUT = create(
        "chemical/x-macromodel-input",
        "mmd",
        "mmod"
    );
    /** CHEMICAL_X_MDL_MOLFILE media type constant. */
    public static final MediaType CHEMICAL_X_MDL_MOLFILE = create(
        "chemical/x-mdl-molfile",
        "mol"
    );
    /** CHEMICAL_X_MDL_RDFILE media type constant. */
    public static final MediaType CHEMICAL_X_MDL_RDFILE = create(
        "chemical/x-mdl-rdfile",
        "rd"
    );
    /** CHEMICAL_X_MDL_RXNFILE media type constant. */
    public static final MediaType CHEMICAL_X_MDL_RXNFILE = create(
        "chemical/x-mdl-rxnfile",
        "rxn"
    );
    /** CHEMICAL_X_MDL_SDFILE media type constant. */
    public static final MediaType CHEMICAL_X_MDL_SDFILE = create(
        "chemical/x-mdl-sdfile",
        "sd",
        "sdf"
    );
    /** CHEMICAL_X_MDL_TGF media type constant. */
    public static final MediaType CHEMICAL_X_MDL_TGF = create(
        "chemical/x-mdl-tgf",
        "tgf"
    );
    /** CHEMICAL_X_MIF media type constant. */
    public static final MediaType CHEMICAL_X_MIF = create(
        "chemical/x-mif",
        "mif"
    );
    /** CHEMICAL_X_MMCIF media type constant. */
    public static final MediaType CHEMICAL_X_MMCIF = create(
        "chemical/x-mmcif",
        "mcif"
    );
    /** CHEMICAL_X_MOL2 media type constant. */
    public static final MediaType CHEMICAL_X_MOL2 = create(
        "chemical/x-mol2",
        "mol2"
    );
    /** CHEMICAL_X_MOLCONN_Z media type constant. */
    public static final MediaType CHEMICAL_X_MOLCONN_Z = create(
        "chemical/x-molconn-z",
        "b"
    );
    /** CHEMICAL_X_MOPAC_GRAPH media type constant. */
    public static final MediaType CHEMICAL_X_MOPAC_GRAPH = create(
        "chemical/x-mopac-graph",
        "gpt"
    );
    /** CHEMICAL_X_MOPAC_INPUT media type constant. */
    public static final MediaType CHEMICAL_X_MOPAC_INPUT = create(
        "chemical/x-mopac-input",
        "mop",
        "mopcrt",
        "mpc",
        "zmt"
    );
    /** CHEMICAL_X_MOPAC_OUT media type constant. */
    public static final MediaType CHEMICAL_X_MOPAC_OUT = create(
        "chemical/x-mopac-out",
        "moo"
    );
    /** CHEMICAL_X_MOPAC_VIB media type constant. */
    public static final MediaType CHEMICAL_X_MOPAC_VIB = create(
        "chemical/x-mopac-vib",
        "mvb"
    );
    /** CHEMICAL_X_NCBI_ASN1_ASCII media type constant. */
    public static final MediaType CHEMICAL_X_NCBI_ASN1_ASCII = create(
        "chemical/x-ncbi-asn1-ascii",
        "prt",
        "ent"
    );
    /** CHEMICAL_X_NCBI_ASN1 media type constant. */
    public static final MediaType CHEMICAL_X_NCBI_ASN1 = create(
        "chemical/x-ncbi-asn1",
        "asn"
    );
    /** CHEMICAL_X_NCBI_ASN1_BINARY media type constant. */
    public static final MediaType CHEMICAL_X_NCBI_ASN1_BINARY = create(
        "chemical/x-ncbi-asn1-binary",
        "val",
        "aso"
    );
    /** CHEMICAL_X_NCBI_ASN1_SPEC media type constant. */
    public static final MediaType CHEMICAL_X_NCBI_ASN1_SPEC = create(
        "chemical/x-ncbi-asn1-spec",
        "asn"
    );
    /** CHEMICAL_X_PDB media type constant. */
    public static final MediaType CHEMICAL_X_PDB = create(
        "chemical/x-pdb",
        "pdb",
        "ent"
    );
    /** CHEMICAL_X_ROSDAL media type constant. */
    public static final MediaType CHEMICAL_X_ROSDAL = create(
        "chemical/x-rosdal",
        "ros"
    );
    /** CHEMICAL_X_SWISSPROT media type constant. */
    public static final MediaType CHEMICAL_X_SWISSPROT = create(
        "chemical/x-swissprot",
        "sw"
    );
    /** CHEMICAL_X_VAMAS_ISO14976 media type constant. */
    public static final MediaType CHEMICAL_X_VAMAS_ISO14976 = create(
        "chemical/x-vamas-iso14976",
        "vms"
    );
    /** CHEMICAL_X_VMD media type constant. */
    public static final MediaType CHEMICAL_X_VMD = create(
        "chemical/x-vmd",
        "vmd"
    );
    /** CHEMICAL_X_XTEL media type constant. */
    public static final MediaType CHEMICAL_X_XTEL = create(
        "chemical/x-xtel",
        "xtel"
    );
    /** CHEMICAL_X_XYZ media type constant. */
    public static final MediaType CHEMICAL_X_XYZ = create(
        "chemical/x-xyz",
        "xyz"
    );
    /** The {@code image/avif} media type. */
    public static final MediaType IMAGE_AVIF = create("image/avif", "avif");
    /** The {@code image/bmp} media type. */
    public static final MediaType IMAGE_BMP = create("image/bmp", "bmp");
    /** The {@code image/cgm} media type. */
    public static final MediaType IMAGE_CGM = create("image/cgm", "cgm");
    /** The {@code image/g3fax} media type. */
    public static final MediaType IMAGE_G3FAX = create("image/g3fax", "g3");
    /** The {@code image/heic} media type. */
    public static final MediaType IMAGE_HEIC = create("image/heic", "heic");
    /** The {@code image/gif} media type. */
    public static final MediaType IMAGE_GIF = create("image/gif", "gif");
    /** The {@code image/ief} media type. */
    public static final MediaType IMAGE_IEF = create("image/ief", "ief");
    /** The {@code image/jpeg} media type. */
    public static final MediaType IMAGE_JPEG = create(
        "image/jpeg",
        "jpg",
        "jpeg",
        "jpe"
    );
    /** The {@code image/jxl} media type. */
    public static final MediaType IMAGE_JXL = create("image/jxl", "jxl");
    /** The {@code image/jbig} media type. */
    public static final MediaType IMAGE_JBIG = create(
        "image/jbig",
        "jbig",
        "jbg"
    );
    /** The {@code image/ktx} media type. */
    public static final MediaType IMAGE_KTX = create("image/ktx", "ktx");
    /** The {@code image/pcx} media type. */
    public static final MediaType IMAGE_PCX = create("image/pcx", "pcx");
    /** The {@code image/png} media type. */
    public static final MediaType IMAGE_PNG = create("image/png", "png");
    /** The {@code image/prs-btif} media type. */
    public static final MediaType IMAGE_PRS_BTIF = create(
        "image/prs.btif",
        "btif"
    );
    /** The {@code image/sgi} media type. */
    public static final MediaType IMAGE_SGI = create("image/sgi", "sgi");
    /** The {@code image/svg-xml} media type. */
    public static final MediaType IMAGE_SVG_XML = create(
        "image/svg+xml",
        "svg",
        "svgz"
    );
    /** The {@code image/tiff} media type. */
    public static final MediaType IMAGE_TIFF = create(
        "image/tiff",
        "tiff",
        "tif"
    );
    /** The {@code image/vnd-adobe-photoshop} media type. */
    public static final MediaType IMAGE_VND_ADOBE_PHOTOSHOP = create(
        "image/vnd.adobe.photoshop",
        "psd"
    );
    /** The {@code image/vnd-dece-graphic} media type. */
    public static final MediaType IMAGE_VND_DECE_GRAPHIC = create(
        "image/vnd.dece.graphic",
        "uvi",
        "uvvi",
        "uvg",
        "uvvg"
    );
    /** The {@code image/vnd-djvu} media type. */
    public static final MediaType IMAGE_VND_DJVU = create(
        "image/vnd.djvu",
        "djvu",
        "djv"
    );
    /** The {@code image/vnd-dvb-subtitle} media type. */
    public static final MediaType IMAGE_VND_DVB_SUBTITLE = create(
        "image/vnd.dvb.subtitle",
        "sub"
    );
    /** The {@code image/vnd-dwg} media type. */
    public static final MediaType IMAGE_VND_DWG = create(
        "image/vnd.dwg",
        "dwg"
    );
    /** The {@code image/vnd-dxf} media type. */
    public static final MediaType IMAGE_VND_DXF = create(
        "image/vnd.dxf",
        "dxf"
    );
    /** The {@code image/vnd-fastbidsheet} media type. */
    public static final MediaType IMAGE_VND_FASTBIDSHEET = create(
        "image/vnd.fastbidsheet",
        "fbs"
    );
    /** The {@code image/vnd-fpx} media type. */
    public static final MediaType IMAGE_VND_FPX = create(
        "image/vnd.fpx",
        "fpx"
    );
    /** The {@code image/vnd-fst} media type. */
    public static final MediaType IMAGE_VND_FST = create(
        "image/vnd.fst",
        "fst"
    );
    /** The {@code image/vnd-fujixerox-edmics-mmr} media type. */
    public static final MediaType IMAGE_VND_FUJIXEROX_EDMICS_MMR = create(
        "image/vnd.fujixerox.edmics-mmr",
        "mmr"
    );
    /** The {@code image/vnd-fujixerox-edmics-rlc} media type. */
    public static final MediaType IMAGE_VND_FUJIXEROX_EDMICS_RLC = create(
        "image/vnd.fujixerox.edmics-rlc",
        "rlc"
    );
    /** The {@code image/vnd-ms-modi} media type. */
    public static final MediaType IMAGE_VND_MS_MODI = create(
        "image/vnd.ms-modi",
        "mdi"
    );
    /** The {@code image/vnd-ms-photo} media type. */
    public static final MediaType IMAGE_VND_MS_PHOTO = create(
        "image/vnd.ms-photo",
        "wdp"
    );
    /** The {@code image/vnd-net-fpx} media type. */
    public static final MediaType IMAGE_VND_NET_FPX = create(
        "image/vnd.net-fpx",
        "npx"
    );
    /** The {@code image/vnd-wap-wbmp} media type. */
    public static final MediaType IMAGE_VND_WAP_WBMP = create(
        "image/vnd.wap.wbmp",
        "wbmp"
    );
    /** The {@code image/vnd-xiff} media type. */
    public static final MediaType IMAGE_VND_XIFF = create(
        "image/vnd.xiff",
        "xif"
    );
    /** The {@code image/webp} media type. */
    public static final MediaType IMAGE_WEBP = create("image/webp", "webp");
    /** The {@code image/x-3ds} media type. */
    public static final MediaType IMAGE_X_3DS = create("image/x-3ds", "3ds");
    /** The {@code image/x-canon-cr2} media type. */
    public static final MediaType IMAGE_X_CANON_CR2 = create(
        "image/x-canon-cr2",
        "cr2"
    );
    /** The {@code image/x-canon-crw} media type. */
    public static final MediaType IMAGE_X_CANON_CRW = create(
        "image/x-canon-crw",
        "crw"
    );
    /** The {@code image/x-cmu-raster} media type. */
    public static final MediaType IMAGE_X_CMU_RASTER = create(
        "image/x-cmu-raster",
        "ras"
    );
    /** The {@code image/x-cmx} media type. */
    public static final MediaType IMAGE_X_CMX = create("image/x-cmx", "cmx");
    /** The {@code image/x-coreldraw} media type. */
    public static final MediaType IMAGE_X_CORELDRAW = create(
        "image/x-coreldraw",
        "cdr"
    );
    /** The {@code image/x-coreldrawpattern} media type. */
    public static final MediaType IMAGE_X_CORELDRAWPATTERN = create(
        "image/x-coreldrawpattern",
        "pat"
    );
    /** The {@code image/x-coreldrawtemplate} media type. */
    public static final MediaType IMAGE_X_CORELDRAWTEMPLATE = create(
        "image/x-coreldrawtemplate",
        "cdt"
    );
    /** The {@code image/x-corelphotopaint} media type. */
    public static final MediaType IMAGE_X_CORELPHOTOPAINT = create(
        "image/x-corelphotopaint",
        "cpt"
    );
    /** The {@code image/x-epson-erf} media type. */
    public static final MediaType IMAGE_X_EPSON_ERF = create(
        "image/x-epson-erf",
        "erf"
    );
    /** The {@code image/x-freehand} media type. */
    public static final MediaType IMAGE_X_FREEHAND = create(
        "image/x-freehand",
        "fh",
        "fhc",
        "fh4",
        "fh5",
        "fh7"
    );
    /** The {@code image/x-icon} media type. */
    public static final MediaType IMAGE_X_ICON = create("image/x-icon", "ico");
    /** The {@code image/x-jg} media type. */
    public static final MediaType IMAGE_X_JG = create("image/x-jg", "art");
    /** The {@code image/x-jng} media type. */
    public static final MediaType IMAGE_X_JNG = create("image/x-jng", "jng");
    /** The {@code image/x-mrsid-image} media type. */
    public static final MediaType IMAGE_X_MRSID_IMAGE = create(
        "image/x-mrsid-image",
        "sid"
    );
    /** The {@code image/x-nikon-nef} media type. */
    public static final MediaType IMAGE_X_NIKON_NEF = create(
        "image/x-nikon-nef",
        "nef"
    );
    /** The {@code image/x-olympus-orf} media type. */
    public static final MediaType IMAGE_X_OLYMPUS_ORF = create(
        "image/x-olympus-orf",
        "orf"
    );
    /** The {@code image/x-pcx} media type. */
    public static final MediaType IMAGE_X_PCX = create("image/x-pcx", "pcx");
    /** The {@code image/x-photoshop} media type. */
    public static final MediaType IMAGE_X_PHOTOSHOP = create(
        "image/x-photoshop",
        "psd"
    );
    /** The {@code image/x-pict} media type. */
    public static final MediaType IMAGE_X_PICT = create(
        "image/x-pict",
        "pic",
        "pct"
    );
    /** The {@code image/x-portable-anymap} media type. */
    public static final MediaType IMAGE_X_PORTABLE_ANYMAP = create(
        "image/x-portable-anymap",
        "pnm"
    );
    /** The {@code image/x-portable-bitmap} media type. */
    public static final MediaType IMAGE_X_PORTABLE_BITMAP = create(
        "image/x-portable-bitmap",
        "pbm"
    );
    /** The {@code image/x-portable-graymap} media type. */
    public static final MediaType IMAGE_X_PORTABLE_GRAYMAP = create(
        "image/x-portable-graymap",
        "pgm"
    );
    /** The {@code image/x-portable-pixmap} media type. */
    public static final MediaType IMAGE_X_PORTABLE_PIXMAP = create(
        "image/x-portable-pixmap",
        "ppm"
    );
    /** The {@code image/x-rgb} media type. */
    public static final MediaType IMAGE_X_RGB = create("image/x-rgb", "rgb");
    /** The {@code image/x-tga} media type. */
    public static final MediaType IMAGE_X_TGA = create("image/x-tga", "tga");
    /** The {@code image/x-xbitmap} media type. */
    public static final MediaType IMAGE_X_XBITMAP = create(
        "image/x-xbitmap",
        "xbm"
    );
    /** The {@code image/x-xpixmap} media type. */
    public static final MediaType IMAGE_X_XPIXMAP = create(
        "image/x-xpixmap",
        "xpm"
    );
    /** The {@code image/x-xwindowdump} media type. */
    public static final MediaType IMAGE_X_XWINDOWDUMP = create(
        "image/x-xwindowdump",
        "xwd"
    );
    /** The {@code message/rfc822} media type. */
    public static final MediaType MESSAGE_RFC822 = create(
        "message/rfc822",
        "eml",
        "mime"
    );
    /** The {@code model/iges} media type. */
    public static final MediaType MODEL_IGES = create(
        "model/iges",
        "igs",
        "iges"
    );
    /** The {@code model/mesh} media type. */
    public static final MediaType MODEL_MESH = create(
        "model/mesh",
        "msh",
        "mesh",
        "silo"
    );
    /** The {@code model/material} media type. */
    public static final MediaType MODEL_MATERIAL = create("model/mtl", "mtl");
    /** The {@code model/gltf-json} media type. */
    public static final MediaType MODEL_GLTF_JSON = create(
        "model/gltf+json",
        "gltf"
    );
    /** The {@code model/gltf-binary} media type. */
    public static final MediaType MODEL_GLTF_BINARY = create(
        "model/gltf+binary",
        "glb"
    );
    /** The {@code model/obj} media type. */
    public static final MediaType MODEL_OBJ = create("model/obj", "obj");
    /** The {@code model/usd} media type. */
    public static final MediaType MODEL_USD = create("model/usd", "usd");
    /** The {@code model/vnd-collada-xml} media type. */
    public static final MediaType MODEL_VND_COLLADA_XML = create(
        "model/vnd.collada+xml",
        "dae"
    );
    /** The {@code model/vnd-dwf} media type. */
    public static final MediaType MODEL_VND_DWF = create(
        "model/vnd.dwf",
        "dwf"
    );
    /** The {@code model/vnd-gdl} media type. */
    public static final MediaType MODEL_VND_GDL = create(
        "model/vnd.gdl",
        "gdl"
    );
    /** The {@code model/vnd-gtw} media type. */
    public static final MediaType MODEL_VND_GTW = create(
        "model/vnd.gtw",
        "gtw"
    );
    /** The {@code model/vnd-mts} media type. */
    public static final MediaType MODEL_VND_MTS = create(
        "model/vnd.mts",
        "mts"
    );
    /** The {@code model/vnd-vtu} media type. */
    public static final MediaType MODEL_VND_VTU = create(
        "model/vnd.vtu",
        "vtu"
    );
    /** The {@code model/vnd-usdz-zip} media type. */
    public static final MediaType MODEL_VND_USDZ_ZIP = create(
        "model/vnd.usdz+zip",
        "usdz"
    );
    /** The {@code model/vnd-usda} media type. */
    public static final MediaType MODEL_VND_USDA = create(
        "model/vnd.usda",
        "usda"
    );
    /** The {@code model/vrml} media type. */
    public static final MediaType MODEL_VRML = create(
        "model/vrml",
        "wrl",
        "vrml"
    );
    /** The {@code model/x3d-binary} media type. */
    public static final MediaType MODEL_X3D_BINARY = create(
        "model/x3d+binary",
        "x3db",
        "x3dbz"
    );
    /** The {@code model/x3d-vrml} media type. */
    public static final MediaType MODEL_X3D_VRML = create(
        "model/x3d+vrml",
        "x3dv",
        "x3dvz"
    );
    /** The {@code model/x3d-xml} media type. */
    public static final MediaType MODEL_X3D_XML = create(
        "model/x3d+xml",
        "x3d",
        "x3dz"
    );
    /** The {@code text/cache-manifest} media type. */
    public static final MediaType TEXT_CACHE_MANIFEST = create(
        "text/cache-manifest",
        "appcache",
        "manifest"
    );
    /** The {@code text/calendar} media type. */
    public static final MediaType TEXT_CALENDAR = create(
        "text/calendar",
        "ics",
        "icz",
        "ifb"
    );
    /** The {@code text/css with UTF-8 charset} media type. */
    public static final MediaType TEXT_CSS_UTF8 = createUTF8("text/css", "css");
    /** The {@code text/csv with UTF-8 charset} media type. */
    public static final MediaType TEXT_CSV_UTF8 = createUTF8("text/csv", "csv");
    /** The {@code text/h323} media type. */
    public static final MediaType TEXT_H323 = create("text/h323", "323");
    /** The {@code text/html with UTF-8 charset} media type. */
    public static final MediaType TEXT_HTML_UTF8 = createUTF8(
        "text/html",
        "html",
        "htm",
        "shtml"
    );
    /** The {@code text/iuls} media type. */
    public static final MediaType TEXT_IULS = create("text/iuls", "uls");
    /** The {@code text/mathml} media type. */
    public static final MediaType TEXT_MATHML = create("text/mathml", "mml");
    /** The {@code text/n3} media type. */
    public static final MediaType TEXT_N3 = create("text/n3", "n3");
    /** The {@code text/plain with UTF-8 charset} media type. */
    public static final MediaType TEXT_PLAIN_UTF8 = createUTF8(
        "text/plain",
        "asc",
        "txt",
        "text",
        "conf",
        "def",
        "pot",
        "brf",
        "LIST",
        "LOG",
        "IN"
    );
    /** The {@code text/prs-lines-tag} media type. */
    public static final MediaType TEXT_PRS_LINES_TAG = create(
        "text/prs.lines.tag",
        "dsc"
    );
    /** The {@code text/richtext} media type. */
    public static final MediaType TEXT_RICHTEXT = create(
        "text/richtext",
        "rtx"
    );
    /** The {@code text/scriptlet} media type. */
    public static final MediaType TEXT_SCRIPTLET = create(
        "text/scriptlet",
        "sct",
        "wsc"
    );
    /** The {@code text/sgml} media type. */
    public static final MediaType TEXT_SGML = create(
        "text/sgml",
        "sgml",
        "sgm"
    );
    /** The {@code text/tab-separated-values with UTF-8 charset} media type. */
    public static final MediaType TEXT_TAB_SEPARATED_VALUES_UTF8 = createUTF8(
        "text/tab-separated-values",
        "tsv"
    );
    /** The {@code text/texmacs} media type. */
    public static final MediaType TEXT_TEXMACS = create("text/texmacs", "tm");
    /** The {@code text/troff} media type. */
    public static final MediaType TEXT_TROFF = create(
        "text/troff",
        "t",
        "tr",
        "roff",
        "man",
        "me",
        "ms"
    );
    /** The {@code text/turtle} media type. */
    public static final MediaType TEXT_TURTLE = create("text/turtle", "ttl");
    /** The {@code text/uri-list} media type. */
    public static final MediaType TEXT_URI_LIST = create(
        "text/uri-list",
        "uri",
        "uris",
        "urls"
    );
    /** The {@code text/vcard} media type. */
    public static final MediaType TEXT_VCARD = create("text/vcard", "vcard");
    /** The {@code text/vnd-curl} media type. */
    public static final MediaType TEXT_VND_CURL = create(
        "text/vnd.curl",
        "curl"
    );
    /** The {@code text/vnd-curl-dcurl} media type. */
    public static final MediaType TEXT_VND_CURL_DCURL = create(
        "text/vnd.curl.dcurl",
        "dcurl"
    );
    /** The {@code text/vnd-curl-mcurl} media type. */
    public static final MediaType TEXT_VND_CURL_MCURL = create(
        "text/vnd.curl.mcurl",
        "mcurl"
    );
    /** The {@code text/vnd-curl-scurl} media type. */
    public static final MediaType TEXT_VND_CURL_SCURL = create(
        "text/vnd.curl.scurl",
        "scurl"
    );
    /** The {@code text/vnd-dvb-subtitle} media type. */
    public static final MediaType TEXT_VND_DVB_SUBTITLE = create(
        "text/vnd.dvb.subtitle",
        "sub"
    );
    /** The {@code text/vnd-fly} media type. */
    public static final MediaType TEXT_VND_FLY = create("text/vnd.fly", "fly");
    /** The {@code text/vnd-fmi-flexstor} media type. */
    public static final MediaType TEXT_VND_FMI_FLEXSTOR = create(
        "text/vnd.fmi.flexstor",
        "flx"
    );
    /** The {@code text/vnd-graphviz} media type. */
    public static final MediaType TEXT_VND_GRAPHVIZ = create(
        "text/vnd.graphviz",
        "gv"
    );
    /** The {@code text/vnd-in3d-3dml} media type. */
    public static final MediaType TEXT_VND_IN3D_3DML = create(
        "text/vnd.in3d.3dml",
        "3dml"
    );
    /** The {@code text/vnd-in3d-spot} media type. */
    public static final MediaType TEXT_VND_IN3D_SPOT = create(
        "text/vnd.in3d.spot",
        "spot"
    );
    /** The {@code text/vnd-sun-j2me-app-descriptor} media type. */
    public static final MediaType TEXT_VND_SUN_J2ME_APP_DESCRIPTOR = create(
        "text/vnd.sun.j2me.app-descriptor",
        "jad"
    );
    /** The {@code text/vnd-wap-wmlscript} media type. */
    public static final MediaType TEXT_VND_WAP_WMLSCRIPT = create(
        "text/vnd.wap.wmlscript",
        "wmls"
    );
    /** The {@code text/vnd-wap-wml} media type. */
    public static final MediaType TEXT_VND_WAP_WML = create(
        "text/vnd.wap.wml",
        "wml"
    );
    /** The {@code text/x-asm with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_ASM_UTF8 = createUTF8(
        "text/x-asm",
        "s",
        "asm"
    );
    /** The {@code text/x-bibtex with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_BIBTEX_UTF8 = createUTF8(
        "text/x-bibtex",
        "bib"
    );
    /** The {@code text/x-boo with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_BOO_UTF8 = createUTF8(
        "text/x-boo",
        "boo"
    );
    /** The {@code text/x-c with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_C_UTF8 = createUTF8(
        "text/x-c",
        "c",
        "cc",
        "cxx",
        "cpp",
        "h",
        "hh",
        "dic"
    );
    /** The {@code text/x-chdr with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_CHDR_UTF8 = createUTF8(
        "text/x-chdr",
        "h"
    );
    /** The {@code text/x-c--hdr with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_C__HDR_UTF8 = createUTF8(
        "text/x-c++hdr",
        "h++",
        "hpp",
        "hxx",
        "hh"
    );
    /** The {@code text/x-component with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_COMPONENT_UTF8 = createUTF8(
        "text/x-component",
        "htc"
    );
    /** The {@code text/x-csh with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_CSH_UTF8 = createUTF8(
        "text/x-csh",
        "csh"
    );
    /** The {@code text/x-csrc with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_CSRC_UTF8 = createUTF8(
        "text/x-csrc",
        "c"
    );
    /** The {@code text/x-c--src with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_C__SRC_UTF8 = createUTF8(
        "text/x-c++src",
        "c++",
        "cpp",
        "cxx",
        "cc"
    );
    /** The {@code text/x-diff with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_DIFF_UTF8 = createUTF8(
        "text/x-diff",
        "diff",
        "patch"
    );
    /** The {@code text/x-dsrc with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_DSRC_UTF8 = createUTF8(
        "text/x-dsrc",
        "d"
    );
    /** The {@code text/x-fortran with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_FORTRAN_UTF8 = createUTF8(
        "text/x-fortran",
        "f",
        "for",
        "f77",
        "f90"
    );
    /** The {@code text/x-haskell with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_HASKELL_UTF8 = createUTF8(
        "text/x-haskell",
        "hs"
    );
    /** The {@code text/x-java with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_JAVA_UTF8 = createUTF8(
        "text/x-java",
        "java"
    );
    /** The {@code text/x-java-source with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_JAVA_SOURCE_UTF8 = createUTF8(
        "text/x-java-source",
        "java"
    );
    /** The {@code text/x-literate-haskell with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_LITERATE_HASKELL_UTF8 = createUTF8(
        "text/x-literate-haskell",
        "lhs"
    );
    /** The {@code text/x-moc with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_MOC_UTF8 = createUTF8(
        "text/x-moc",
        "moc"
    );
    /** The {@code text/x-nfo with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_NFO_UTF8 = createUTF8(
        "text/x-nfo",
        "nfo"
    );
    /** The {@code text/x-opml with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_OPML_UTF8 = createUTF8(
        "text/x-opml",
        "opml"
    );
    /** The {@code text/x-pascal with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_PASCAL_UTF8 = createUTF8(
        "text/x-pascal",
        "p",
        "pas"
    );
    /** The {@code text/x-pcs-gcd with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_PCS_GCD_UTF8 = createUTF8(
        "text/x-pcs-gcd",
        "gcd"
    );
    /** The {@code text/x-perl with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_PERL_UTF8 = createUTF8(
        "text/x-perl",
        "pl",
        "pm"
    );
    /** The {@code text/x-python with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_PYTHON_UTF8 = createUTF8(
        "text/x-python",
        "py"
    );
    /** The {@code text/x-scala with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_SCALA_UTF8 = createUTF8(
        "text/x-scala",
        "scala"
    );
    /** The {@code text/x-setext with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_SETEXT_UTF8 = createUTF8(
        "text/x-setext",
        "etx"
    );
    /** The {@code text/x-sfv with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_SFV_UTF8 = createUTF8(
        "text/x-sfv",
        "sfv"
    );
    /** The {@code text/x-sh with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_SH_UTF8 = createUTF8(
        "text/x-sh",
        "sh"
    );
    /** The {@code text/x-tcl with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_TCL_UTF8 = createUTF8(
        "text/x-tcl",
        "tcl",
        "tk"
    );
    /** The {@code text/x-tex with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_TEX_UTF8 = createUTF8(
        "text/x-tex",
        "tex",
        "ltx",
        "sty",
        "cls"
    );
    /** The {@code text/x-uuencode with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_UUENCODE_UTF8 = createUTF8(
        "text/x-uuencode",
        "uu"
    );
    /** The {@code text/x-vcalendar with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_VCALENDAR_UTF8 = createUTF8(
        "text/x-vcalendar",
        "vcs"
    );
    /** The {@code text/x-vcard with UTF-8 charset} media type. */
    public static final MediaType TEXT_X_VCARD_UTF8 = createUTF8(
        "text/x-vcard",
        "vcf"
    );
    /** The {@code video/3gpp2} media type. */
    public static final MediaType VIDEO_3GPP2 = create("video/3gpp2", "3g2");
    /** The {@code video/3gpp} media type. */
    public static final MediaType VIDEO_3GPP = create("video/3gpp", "3gp");
    /** The {@code video/annodex} media type. */
    public static final MediaType VIDEO_ANNODEX = create(
        "video/annodex",
        "axv"
    );
    /** The {@code video/dl} media type. */
    public static final MediaType VIDEO_DL = create("video/dl", "dl");
    /** The {@code video/dv} media type. */
    public static final MediaType VIDEO_DV = create("video/dv", "dif", "dv");
    /** The {@code video/fli} media type. */
    public static final MediaType VIDEO_FLI = create("video/fli", "fli");
    /** The {@code video/gl} media type. */
    public static final MediaType VIDEO_GL = create("video/gl", "gl");
    /** The {@code video/h261} media type. */
    public static final MediaType VIDEO_H261 = create("video/h261", "h261");
    /** The {@code video/h263} media type. */
    public static final MediaType VIDEO_H263 = create("video/h263", "h263");
    /** The {@code video/h264} media type. */
    public static final MediaType VIDEO_H264 = create("video/h264", "h264");
    /** The {@code video/jpeg} media type. */
    public static final MediaType VIDEO_JPEG = create("video/jpeg", "jpgv");
    /** The {@code video/jpm} media type. */
    public static final MediaType VIDEO_JPM = create(
        "video/jpm",
        "jpm",
        "jpgm"
    );
    /** The {@code video/mj2} media type. */
    public static final MediaType VIDEO_MJ2 = create(
        "video/mj2",
        "mj2",
        "mjp2"
    );
    /** The {@code video/mp2t} media type. */
    public static final MediaType VIDEO_MP2T = create("video/mp2t", "ts");
    /** The {@code video/mp4} media type. */
    public static final MediaType VIDEO_MP4 = create(
        "video/mp4",
        "mp4",
        "mp4v",
        "mpg4"
    );
    /** The {@code video/mpeg} media type. */
    public static final MediaType VIDEO_MPEG = create(
        "video/mpeg",
        "mpeg",
        "mpg",
        "mpe",
        "m1v",
        "m2v"
    );
    /** The {@code video/ogg} media type. */
    public static final MediaType VIDEO_OGG = create("video/ogg", "ogv");
    /** The {@code video/quicktime} media type. */
    public static final MediaType VIDEO_QUICKTIME = create(
        "video/quicktime",
        "qt",
        "mov"
    );
    /** The {@code video/vnd-dece-hd} media type. */
    public static final MediaType VIDEO_VND_DECE_HD = create(
        "video/vnd.dece.hd",
        "uvh",
        "uvvh"
    );
    /** The {@code video/vnd-dece-mobile} media type. */
    public static final MediaType VIDEO_VND_DECE_MOBILE = create(
        "video/vnd.dece.mobile",
        "uvm",
        "uvvm"
    );
    /** The {@code video/vnd-dece-pd} media type. */
    public static final MediaType VIDEO_VND_DECE_PD = create(
        "video/vnd.dece.pd",
        "uvp",
        "uvvp"
    );
    /** The {@code video/vnd-dece-sd} media type. */
    public static final MediaType VIDEO_VND_DECE_SD = create(
        "video/vnd.dece.sd",
        "uvs",
        "uvvs"
    );
    /** The {@code video/vnd-dece-video} media type. */
    public static final MediaType VIDEO_VND_DECE_VIDEO = create(
        "video/vnd.dece.video",
        "uvv",
        "uvvv"
    );
    /** The {@code video/vnd-dvb-file} media type. */
    public static final MediaType VIDEO_VND_DVB_FILE = create(
        "video/vnd.dvb.file",
        "dvb"
    );
    /** The {@code video/vnd-fvt} media type. */
    public static final MediaType VIDEO_VND_FVT = create(
        "video/vnd.fvt",
        "fvt"
    );
    /** The {@code video/vnd-mpegurl} media type. */
    public static final MediaType VIDEO_VND_MPEGURL = create(
        "video/vnd.mpegurl",
        "mxu",
        "m4u"
    );
    /** The {@code video/vnd-ms-playready-media-pyv} media type. */
    public static final MediaType VIDEO_VND_MS_PLAYREADY_MEDIA_PYV = create(
        "video/vnd.ms-playready.media.pyv",
        "pyv"
    );
    /** The {@code video/vnd-uvvu-mp4} media type. */
    public static final MediaType VIDEO_VND_UVVU_MP4 = create(
        "video/vnd.uvvu.mp4",
        "uvu",
        "uvvu"
    );
    /** The {@code video/vnd-vivo} media type. */
    public static final MediaType VIDEO_VND_VIVO = create(
        "video/vnd.vivo",
        "viv"
    );
    /** The {@code video/webm} media type. */
    public static final MediaType VIDEO_WEBM = create("video/webm", "webm");
    /** The {@code video/x-f4v} media type. */
    public static final MediaType VIDEO_X_F4V = create("video/x-f4v", "f4v");
    /** The {@code video/x-fli} media type. */
    public static final MediaType VIDEO_X_FLI = create("video/x-fli", "fli");
    /** The {@code video/x-flv} media type. */
    public static final MediaType VIDEO_X_FLV = create("video/x-flv", "flv");
    /** The {@code video/x-la-asf} media type. */
    public static final MediaType VIDEO_X_LA_ASF = create(
        "video/x-la-asf",
        "lsf",
        "lsx"
    );
    /** The {@code video/x-m4v} media type. */
    public static final MediaType VIDEO_X_M4V = create("video/x-m4v", "m4v");
    /** The {@code video/x-matroska} media type. */
    public static final MediaType VIDEO_X_MATROSKA = create(
        "video/x-matroska",
        "mpv",
        "mkv",
        "mk3d",
        "mks"
    );
    /** The {@code video/x-mng} media type. */
    public static final MediaType VIDEO_X_MNG = create("video/x-mng", "mng");
    /** The {@code video/x-ms-asf} media type. */
    public static final MediaType VIDEO_X_MS_ASF = create(
        "video/x-ms-asf",
        "asf",
        "asx"
    );
    /** The {@code video/x-msvideo} media type. */
    public static final MediaType VIDEO_X_MSVIDEO = create(
        "video/x-msvideo",
        "avi"
    );
    /** The {@code video/x-ms-vob} media type. */
    public static final MediaType VIDEO_X_MS_VOB = create(
        "video/x-ms-vob",
        "vob"
    );
    /** The {@code video/x-ms-wmv} media type. */
    public static final MediaType VIDEO_X_MS_WMV = create(
        "video/x-ms-wmv",
        "wmv"
    );
    /** The {@code video/x-ms-wm} media type. */
    public static final MediaType VIDEO_X_MS_WM = create("video/x-ms-wm", "wm");
    /** The {@code video/x-ms-wmx} media type. */
    public static final MediaType VIDEO_X_MS_WMX = create(
        "video/x-ms-wmx",
        "wmx"
    );
    /** The {@code video/x-ms-wvx} media type. */
    public static final MediaType VIDEO_X_MS_WVX = create(
        "video/x-ms-wvx",
        "wvx"
    );
    /** The {@code video/x-sgi-movie} media type. */
    public static final MediaType VIDEO_X_SGI_MOVIE = create(
        "video/x-sgi-movie",
        "movie"
    );
    /** The {@code video/x-smv} media type. */
    public static final MediaType VIDEO_X_SMV = create("video/x-smv", "smv");
    /** The {@code x-conference/x-cooltalk} media type. */
    public static final MediaType X_CONFERENCE_X_COOLTALK = create(
        "x-conference/x-cooltalk",
        "ice"
    );
    /** The {@code x-epoc/x-sisx-app} media type. */
    public static final MediaType X_EPOC_X_SISX_APP = create(
        "x-epoc/x-sisx-app",
        "sisx"
    );
    /** The {@code x-world/x-vrml} media type. */
    public static final MediaType X_WORLD_X_VRML = create(
        "x-world/x-vrml",
        "vrm",
        "vrml",
        "wrl"
    );

    /*******************************************************/

    /** HTML_UTF_8 media type constant. */
    public static final MediaType HTML_UTF_8 = TEXT_HTML_UTF8;
    /** CSS_UTF_8 media type constant. */
    public static final MediaType CSS_UTF_8 = TEXT_CSS_UTF8;
    /** CSV_UTF_8 media type constant. */
    public static final MediaType CSV_UTF_8 = TEXT_CSV_UTF8;
    /** PLAIN_TEXT_UTF_8 media type constant. */
    public static final MediaType PLAIN_TEXT_UTF_8 = TEXT_PLAIN_UTF8;

    /** XHTML_XML_UTF8 media type constant. */
    public static final MediaType XHTML_XML_UTF8 = APPLICATION_XHTML_XML_UTF8;
    /** JAVASCRIPT_UTF8 media type constant. */
    public static final MediaType JAVASCRIPT_UTF8 = APPLICATION_JAVASCRIPT_UTF8;
    /** JSON media type constant. */
    public static final MediaType JSON = APPLICATION_JSON;
    /** XML_UTF_8 media type constant. */
    public static final MediaType XML_UTF_8 = APPLICATION_XML_UTF8;

    /** BINARY media type constant. */
    public static final MediaType BINARY = APPLICATION_OCTET_STREAM;
    /** ZIP media type constant. */
    public static final MediaType ZIP = APPLICATION_ZIP;
    /** PDF media type constant. */
    public static final MediaType PDF = APPLICATION_PDF;
    /** SWF media type constant. */
    public static final MediaType SWF = APPLICATION_X_SHOCKWAVE_FLASH;

    /** JPEG media type constant. */
    public static final MediaType JPEG = IMAGE_JPEG;
    /** PNG media type constant. */
    public static final MediaType PNG = IMAGE_PNG;
    /** BMP media type constant. */
    public static final MediaType BMP = IMAGE_BMP;
    /** GIF media type constant. */
    public static final MediaType GIF = IMAGE_GIF;
    /** SVG media type constant. */
    public static final MediaType SVG = IMAGE_SVG_XML;
    /** JXL media type constant. */
    public static final MediaType JXL = IMAGE_JXL;

    /** DEFAULT media type constant. */
    public static final MediaType DEFAULT = MediaType.BINARY;

    /*******************************************************/

    /** Media type constant. */
    /**
     * Creates and registers a media type with optional file extensions.
     *
     * @param type the MIME type string
     * @param fileExtensisons file extensions mapped to this media type
     * @return the registered media type
     */
    public static synchronized MediaType create(
        String type,
        String... fileExtensisons
    ) {
        return create(type, NO_ATTR, fileExtensisons);
    }

    /**
     * Creates and registers a media type with attributes and file extensions.
     *
     * @param type the MIME type string
     * @param attributes media type parameters such as charset
     * @param fileExtensions file extensions mapped to this media type
     * @return the registered media type
     */
    public static synchronized MediaType create(
        String type,
        String[] attributes,
        String... fileExtensions
    ) {
        MediaType mt = new MediaType(type, attributes);

        if (fileExtensions == null) {
            fileExtensions = new String[] {};
        }

        if (Arrays.stream(attributes).noneMatch(a -> a.equals(UTF8_ATTR[0]))) {
            for (String ext : fileExtensions) {
                FILE_EXTENSIONS.put(ext, mt);
            }
        }

        return mt;
    }

    /**
     * Creates and registers a UTF-8 media type with optional file extensions.
     *
     * @param type the MIME type string
     * @param fileExtensions file extensions mapped to this media type
     * @return the registered UTF-8 media type
     */
    public static synchronized MediaType createUTF8(
        String type,
        String... fileExtensions
    ) {
        if (fileExtensions == null) {
            fileExtensions = new String[] {};
        }

        for (String ext : fileExtensions) {
            if (!FILE_EXTENSIONS.containsKey(ext)) {
                FILE_EXTENSIONS.put(ext, create(type, fileExtensions));
            }
        }
        return create(type, UTF8_ATTR, fileExtensions);
    }

    /**
     * Looks up a media type by file extension.
     *
     * @param fileExtension the extension without a dot
     * @return the media type, or null when unknown
     */
    public static synchronized MediaType getByFileExtension(
        String fileExtension
    ) {
        return FILE_EXTENSIONS.get(fileExtension);
    }

    /**
     * Looks up a media type by file name extension, defaulting to binary.
     *
     * @param filename the file name
     * @return the mapped media type, or the binary default
     */
    public static synchronized MediaType getByFileName(String filename) {
        int dotPos = filename.lastIndexOf('.');
        if (dotPos >= 0) {
            String ext = filename.substring(dotPos + 1);
            return getByFileExtension(ext);
        } else {
            return MediaType.DEFAULT;
        }
    }

    /**
     * Looks up a media type by MIME string, defaulting to binary.
     *
     * @param type the MIME type string
     * @return the mapped media type, or the binary default
     */
    public static synchronized MediaType getByMimeType(String type) {
        return Optional.ofNullable(getTypeMap().get(type)).orElse(
            MediaType.DEFAULT
        );
    }

    /**
     * Returns a value with the charset applied.
     *
     * @param charset the charset
     * @return this instance
     */
    public String withCharset(String charset) {
        return charset != null
            ? String.format(
                  "%s; charset=%s",
                  this.contentType,
                  charset.toUpperCase()
              )
            : this.contentType;
    }

    /**
     * Creates an instance from the given content type.
     *
     * @param contentType the content type
     * @return this instance
     */
    public static MediaType of(String contentType) {
        return new MediaType(contentType);
    }

    /** The bytes. */
    private final byte[] bytes;

    /** The content type. */
    private final String contentType;

    private MediaType(String contentType) {
        this.bytes = contentType.getBytes();
        this.contentType = contentType;
    }

    private MediaType(String name, String[] attributes) {
        this.bytes = join(name, attributes).getBytes();
        this.contentType = new String(this.bytes);
    }

    private String join(String name, String[] attributes) {
        String attrs = Arrays.stream(attributes).collect(
            Collectors.joining(";")
        );

        return attrs.isEmpty() ? name : name + "; " + attrs;
    }

    /**
     * Returns the bytes.
     *
     * @return the bytes, or null when unset
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Returns the content type.
     *
     * @return the content type, or null when unset
     */
    @JsonProperty("contentType")
    public String contentType() {
        return this.contentType;
    }

    /**
     * Returns the to string.
     *
     * @return the to string, or null when unset
     */
    @Override
    public String toString() {
        return this.contentType;
    }

    /**
     * Returns the file extension.
     *
     * @return the file extension, or null when unset
     */
    public String getFileExtension() {
        return Optional.ofNullable(getFileExtensionMap().get(this))
            .map(fe -> fe.get(0))
            .orElse(null);
    }

    /**
     * Returns the file extensions.
     *
     * @return the file extensions, or null when unset
     */
    public List<String> getFileExtensions() {
        return Optional.ofNullable(getFileExtensionMap().get(this)).orElse(
            null
        );
    }

    /**
     * Returns the sub type.
     *
     * @return the sub type, or null when unset
     */
    public String subType() {
        return this.contentType.substring(
            this.contentType.lastIndexOf("/") + 1,
            Math.max(
                this.contentType.lastIndexOf(";"),
                this.contentType.length()
            )
        );
    }

    /**
     * Returns the base type.
     *
     * @return the base type, or null when unset
     */
    public String baseType() {
        return this.contentType.substring(0, this.contentType.lastIndexOf(";"));
    }

    /** The type_map. */
    private static final Map<String, MediaType> TYPE_MAP = new HashMap<>();

    /** The extension_map. */
    private static final Map<MediaType, List<String>> EXTENSION_MAP =
        new HashMap<>();

    /**
     * Returns the file extension map.
     *
     * @return the file extension map, or null when unset
     */
    public static Map<MediaType, List<String>> getFileExtensionMap() {
        if (EXTENSION_MAP.size() == 0) {
            EXTENSION_MAP.putAll(
                FILE_EXTENSIONS.entrySet()
                    .stream()
                    .collect(
                        Collectors.groupingBy(
                            Map.Entry::getValue,
                            Collectors.mapping(
                                Map.Entry::getKey,
                                Collectors.toList()
                            )
                        )
                    )
            );
        }

        return EXTENSION_MAP;
    }

    /**
     * Returns the type map.
     *
     * @return the type map, or null when unset
     */
    public static Map<String, MediaType> getTypeMap() {
        if (TYPE_MAP.size() == 0) {
            Class<?> clazz = MediaType.class;

            Field[] fields = clazz.getDeclaredFields();

            Arrays.stream(fields)
                .filter(f -> f.getType().equals(MediaType.class))
                .forEach(f -> {
                    try {
                        MediaType mt = (MediaType) f.get(MediaType.class);
                        TYPE_MAP.put(mt.contentType, mt);
                    } catch (Exception e) {}
                });
        }

        return TYPE_MAP;
    }

    /**
     * Returns the hash code.
     *
     * @return the hash code, or null when unset
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bytes);
        return result;
    }

    /**
     * Compares media types by content type bytes.
     *
     * @param obj the object to compare
     * @return true when the media types match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MediaType other = (MediaType) obj;
        if (!Arrays.equals(bytes, other.bytes)) return false;
        return true;
    }

    /**
     * Returns the info.
     *
     * @return the info, or null when unset
     */
    public String info() {
        if (this == HTML_UTF_8) {
            return "html";
        }

        if (this == JSON) {
            return "json";
        }

        if (this == PLAIN_TEXT_UTF_8) {
            return "plain";
        }

        if (this == BINARY) {
            return "binary";
        }

        return toString();
    }
}
