/*
 * JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.XBaseReaderFactory;
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.foxpro.FoxProUtils;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.field.XBaseField;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

public class ReaderWithMemoIT {
    @Test
    @SuppressWarnings("unchecked")
    public <A extends XBaseAccess, D extends XBaseDialect<A, D>> void test1()
            throws IOException, ParseException {
        final String tableName = TestHelper.getResourceTableName("memo1/texto.dbf");

        try {
            final Charset charset = Charset.forName("cp1252");
            final XBaseReader<A, D> reader =
                    (XBaseReader<A, D>) XBaseReaderFactory.createReader(tableName, charset);
            try {
                final XBaseMetadata meta = reader.getMetadata();

                Assert.assertEquals(0x30, meta.getFileTypeByte());
                Assert.assertEquals(XBaseFileTypeEnum.VisualFoxPro, meta.getFileType());
                Assert.assertEquals(5, meta.get(FoxProUtils.META_RECORDS_QTY));
                Assert.assertEquals(488, meta.getFullHeaderLength());
                Assert.assertEquals(274, meta.getOneRecordLength());
                Assert.assertEquals(TestHelper.createDate(114, 6, 4),
                        meta.get(FoxProUtils.META_UPDATE_DATE));
                Assert.assertEquals(6, reader.getFieldDescriptorArray().getFields().size());

                final XBaseDialect<A, D> dialect = reader.getDialect();
                final XBaseFieldDescriptorArray<A> array = reader.getFieldDescriptorArray();

                Assert.assertEquals(VisualFoxProDialect.class, dialect.getClass());
                final A access = dialect.getAccess();
                for (final XBaseField<? super A> field : array.getFields()) {
                    final String name = field.getName();
                    if (name.equals("TEXVER")) {
                        Assert.assertEquals(5, field.getValueLength(access));
                        Assert.assertEquals(CharacterField.class, field.getClass());
                    } else if (name.equals("TEXTEX")) {
                        Assert.assertEquals(4, field.getValueLength(access));
                        Assert.assertEquals(MemoField.class, field.getClass());
                    } else if (name.equals("TEXDAT")) {
                        Assert.assertEquals(8, field.getValueLength(access));
                        Assert.assertEquals(DateField.class, field.getClass());
                    } else if (name.equals("TEXSTA")) {
                        Assert.assertEquals(1, field.getValueLength(access));
                        Assert.assertEquals(CharacterField.class, field.getClass());
                    } else if (name.equals("TEXCAM")) {
                        Assert.assertEquals(254, field.getValueLength(access));
                        Assert.assertEquals(CharacterField.class, field.getClass());
                    }
                }

                XBaseRecord rec;
                int recCount = 0;
                while ((rec = reader.read()) != null) {
                    Assert.assertFalse(rec.isDeleted());
                    Assert.assertEquals(recCount + 1, rec.getRecordNumber());
                    final String texttex =
                            ((TextMemoRecord) rec.getMap().get("TEXTEX")).getValue();
                    if (recCount == 1) {
                        Assert.assertEquals("Alterações\r\n" +
                                "=========\r\n" +
                                "\r\n" +
                                "1. Alteração do tamanho (36) e da descrição do atributo {Id} de todos os eventos.\r\n" +
                                "\r\n" +
                                "2. Alteração do tamanho máximo dos campos {nrInscricao} e {nrInscEstab} para 15 caracteres, para compatibilização com o tamanho máximo de inscrição permitida (CAEPF).\r\n" +
                                "\r\n" +
                                "3. Inclusão da regra de validação REGRA_VALIDA_ID_EVENTO em todos os eventos.\r\n" +
                                "\r\n" +
                                "4. Revisão do texto da regra de validação do campo {fap}\r\n" +
                                "\r\n" +
                                "5. Alteração do nome do evento para “Informações do Empregador/Contribuinte”\r\n" +
                                "\r\n" +
                                "6. Alteração do tamanho do campo {nomeRazao} para 115 caracteres, para ficar compatível com o tamanho máximo do cadastro de CNPJ.\r\n" +
                                "\r\n" +
                                "7. Alteração da obrigatoriedade e da regra de validação dos campos {cnaePreponderante}, {indConstrutora}, {indCooperativa} (os campos só devem ser preenchidos por PJ).\r\n" +
                                "\r\n" +
                                "8. As regras REGRA_INFO_EMP_VALIDA_RAT E REGRA_INFO_EMP_VALIDA_FAP foram removidas do evento. A validação da RAT e FAP\r\n" +
                                "\r\n" +
                                "9. Alteração do formato dos campos iniValidade e fimValidade dos grupos{inclusao},{alteracao},{novaValidade},{exclusao} para AAAA-MM, para ficar compatível com os campos do tipo Data que também foram alterados para AAAA-MM-DD.\r\n" +
                                "\r\n" +
                                "10. Incluída a regra de validação REGRA_PERMITE_APENAS_PROCESSO_JUDICIAL nos registros {ideProcessoCP}, {ideProcessoIRRF}, {ideProcessoFGTS} e {ideProcessoSIND}\r\n" +
                                "\r\n" +
                                "12. Alteração do formato dos campos iniValidade e fimValidade dos grupos {inclusao},{alteracao},{novaValidade},{exclusao} para AAAA-MM, para ficar compatível com os campos do tipo Data que também foram alterados para AAAA-MM-DD.\r\n" +
                                "\r\n" +
                                "13. Alteração do formato dos campos iniValidade e fimValidade dos grupos {inclusao},{alteracao},{novaValidade},{exclusao} para AAAA-MM, para ficar compatível com os campos do tipo Data que também foram alterados para AAAA-MM-DD.\r\n" +
                                "\r\n" +
                                "14. Alteração do formato dos campos iniValidade e fimValidade dos grupos {inclusao},{alteracao},{novaValidade},{exclusao} para AAAA-MM, para ficar compatível com os campos do tipo Data que também foram alterados para AAAA-MM-DD.\r\n" +
                                "\r\n" +
                                "15. Alteração do formato dos campos iniValidade e fimValidade dos grupos {inclusao},{alteracao},{novaValidade},{exclusao} para AAAA-MM, para ficar compatível com os campos do tipo Data que também foram alterados para AAAA-MM-DD.\r\n" +
                                "\r\n" +
                                "16. Alteração da regra de validação do campo {durIntervalo}.\r\n" +
                                "\r\n" +
                                "17. Exclusão dos campos tpJornada, descTpJornada, tpIntervalo e durIntervalo do grupo {dadosHorContratual}\r\n" +
                                "\r\n" +
                                "18. Exclusão do grupo {varHorario}", texttex);
                    }
                    recCount++;
                }

            } finally {
                reader.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } /*catch (final ParseException e) {
            e.printStackTrace();
        }*/
    }

    private File getResourceFile(final String name) {
        return new File(this.getClass().getClassLoader().getResource(name).getFile());
    }
}
