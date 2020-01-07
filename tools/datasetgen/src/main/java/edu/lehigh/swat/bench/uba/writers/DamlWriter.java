/**
 * by Yuanbo Guo
 * Semantic Web and Agent Technology Lab, CSE Department, Lehigh University, USA
 * Copyright (C) 2004
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package edu.lehigh.swat.bench.uba.writers;

import java.io.PrintStream;

import edu.lehigh.swat.bench.uba.GeneratorCallbackTarget;

public class DamlWriter extends RdfWriter {
    private final String ontology;

    /**
     * Creates a new DAML writer
     * 
     * @param callbackTarget
     *            The callback target
     */
    public DamlWriter(GeneratorCallbackTarget callbackTarget, String ontologyUrl) {
        super(callbackTarget);
        this.ontology = ontologyUrl;
    }

    /**
     * Writes the header part, including namespace declarations and imports
     * statements.
     */
    @Override
    protected void writeHeader(PrintStream out) {
        String s;
        s = "xmlns:" + WriterVocabulary.T_RDF_NS + "=\"" + WriterVocabulary.T_RDF_NS_URI + "\"";
        out.println(s);
        s = "xmlns:" + WriterVocabulary.T_RDFS_NS + "=\"" + WriterVocabulary.T_RDFS_NS_URI + "\"";
        out.println(s);
        s = "xmlns:" + WriterVocabulary.T_DAML_NS + "=\"" + WriterVocabulary.T_DAML_NS_URI + "\"";
        out.println(s);
        s = "xmlns:" + WriterVocabulary.T_ONTO_NS + "=\"" + ontology + "#\">";
        out.println(s);
        s = "<" + WriterVocabulary.T_RDF_PREFIX + "Description " + WriterVocabulary.T_RDF_ABOUT + "=\"\">";
        out.println(s);
        s = "<" + WriterVocabulary.T_DAML_PREFIX + "imports " + WriterVocabulary.T_RDF_RES + "=\"" + ontology + "\" />";
        out.println(s);
        s = "</" + WriterVocabulary.T_RDF_PREFIX + "Description>";
        out.println(s);
    }
}