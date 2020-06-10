/*
 *  (C) Copyright 2017 TheOtherP (theotherp@posteo.net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.nzbhydra.mapping.newznab.xml.caps;

import lombok.Data;
import org.nzbhydra.mapping.newznab.xml.Xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "caps")
@Data
public class CapsXmlRoot extends Xml {

    @XmlElement
    private CapsXmlServer server;

    @XmlElement
    private CapsXmlLimits limits;

    @XmlElement
    private CapsXmlRetention retention;

    @XmlElement
    private CapsXmlSearching searching;

    @XmlElement(name = "categories")
    private CapsXmlCategories categories = new CapsXmlCategories(new ArrayList<>());


}
