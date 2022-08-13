package io.sinistral.proteus.modules;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

@Singleton
public class XmlModule extends AbstractModule {

    @Override
    protected void configure() {

        XMLInputFactory inputFactory = new WstxInputFactory();
        inputFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000);

        bind(XMLInputFactory.class).toInstance(inputFactory);

        XMLOutputFactory outputFactory = new WstxOutputFactory();
        outputFactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true);

        bind(XMLOutputFactory.class).toInstance(outputFactory);

        XmlFactory xmlFactory = new XmlFactory(inputFactory, outputFactory);

        XmlMapper xmlMapper = new XmlMapper(xmlFactory);
        xmlMapper.registerModule(new JavaTimeModule())
                 .registerModule(new ParameterNamesModule())
                 .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                 .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);

        bind(XmlMapper.class).toInstance(xmlMapper);

    }
}
