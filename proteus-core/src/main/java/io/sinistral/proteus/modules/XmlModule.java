package io.sinistral.proteus.modules;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.dataformat.xml.XmlFactory;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.XmlWriteFeature;
import tools.jackson.module.blackbird.BlackbirdModule;

@Singleton
public class XmlModule extends AbstractModule {

    @Override
    protected void configure() {
        XMLInputFactory inputFactory = new WstxInputFactory();
        inputFactory.setProperty(
            WstxInputProperties.P_MAX_ATTRIBUTE_SIZE,
            32000
        );

        bind(XMLInputFactory.class).toInstance(inputFactory);

        XMLOutputFactory outputFactory = new WstxOutputFactory();
        outputFactory.setProperty(
            WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT,
            true
        );

        bind(XMLOutputFactory.class).toInstance(outputFactory);

        XmlFactory xmlFactory = new XmlFactory(inputFactory, outputFactory);

        XmlMapper xmlMapper = XmlMapper.builder(xmlFactory)
            .addModule(new BlackbirdModule())
            .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(XmlWriteFeature.WRITE_XML_DECLARATION)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

        bind(XmlMapper.class).toInstance(xmlMapper);
    }
}
