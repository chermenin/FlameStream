/**
 * This file is part of Wikiforia.
 *
 * Wikiforia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Wikiforia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Wikiforia. If not, see <http://www.gnu.org/licenses/>.
 */
 package nlp.wikipedia.lang;

//Autogenerated from Wikimedia sources at 2015-04-16T13:55:11+00:00

public class YoConfig extends TemplateConfig {
	public YoConfig() {
		addNamespaceAlias(-2, "Amóhùnmáwòrán");
		addNamespaceAlias(-1, "Pàtàkì");
		addNamespaceAlias(1, "Ọ̀rọ̀");
		addNamespaceAlias(2, "Oníṣe");
		addNamespaceAlias(3, "Ọ̀rọ̀_oníṣe");
		addNamespaceAlias(5, "Ọ̀rọ̀_Wikipedia");
		addNamespaceAlias(6, "Fáìlì", "Àwòrán");
		addNamespaceAlias(7, "Ọ̀rọ̀_fáìlì", "Ọ̀rọ̀_àwòrán");
		addNamespaceAlias(8, "MediaWiki");
		addNamespaceAlias(9, "Ọ̀rọ̀_mediaWiki");
		addNamespaceAlias(10, "Àdàkọ");
		addNamespaceAlias(11, "Ọ̀rọ̀_àdàkọ");
		addNamespaceAlias(12, "Ìrànlọ́wọ́");
		addNamespaceAlias(13, "Ọ̀rọ̀_ìrànlọ́wọ́");
		addNamespaceAlias(14, "Ẹ̀ka");
		addNamespaceAlias(15, "Ọ̀rọ̀_ẹ̀ka");

	}

	@Override
	protected String getSiteName() {
		return "Wikipedia";
	}

	@Override
	protected String getWikiUrl() {
		return "http://yo.wikipedia.org/";
	}

	@Override
	public String getIso639() {
		return "yo";
	}
}