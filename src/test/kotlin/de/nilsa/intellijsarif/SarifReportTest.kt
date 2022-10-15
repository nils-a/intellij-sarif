package de.nilsa.intellijsarif

import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.nilsa.intellijsarif.json.Result
import de.nilsa.intellijsarif.report.SarifReport

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class SarifReportTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/testData/reports"

    fun `test that the SarifReport should parse the 5 errors json`() {
        val report = SarifReport(project, "$testDataPath/five-errors.sarif.json")

        assertNotNull(report.run)
        val run = report.run!!
        assertEquals("Infer", run.tool.driver.name)
        assertEquals(5, run.results.count())
        val err = run.results.first()
        assertEquals(Result.Level.ERROR, err.level)
    }
}
