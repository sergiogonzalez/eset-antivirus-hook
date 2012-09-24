/**
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.sympo.antivirus;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.documentlibrary.antivirus.AntivirusScannerException;
import com.liferay.portlet.documentlibrary.antivirus.BaseFileAntivirusScanner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sergio González
 */
public class EsetAntivirusScannerImpl extends BaseFileAntivirusScanner {

	public void scan(File file)
		throws AntivirusScannerException, SystemException {

		Runtime runtime = Runtime.getRuntime();

		String filePath = file.getAbsolutePath();

		String antivirusExecutePath = PropsUtil.get("antivirus.execute.path");

		String[] parameters = new String[] {antivirusExecutePath, filePath};

		Process process = null;

		try {
			process = runtime.exec(parameters);

			InputStream inputStream = process.getInputStream();

			String scanResult = StringUtil.read(inputStream);

			process.waitFor();

			int exitValue = process.exitValue();

			if (exitValue == EsetAntivirusScannerConstants.VIRUS_FOUND) {
				if (_log.isErrorEnabled()) {
					_log.error(scanResult);
				}

				throw new AntivirusScannerException(
					"Virus detected in " + filePath);
			}
			else if (exitValue ==
					EsetAntivirusScannerConstants.CANNOT_SCAN_DOCUMENT) {

				if (_log.isErrorEnabled()) {
					_log.error(scanResult);
				}

				throw new AntivirusScannerException(
					"Couldn't scan file in " + filePath);
			}
			else if (exitValue !=
					EsetAntivirusScannerConstants.VIRUS_NOT_FOUND) {

				if (_log.isErrorEnabled()) {
					_log.error(scanResult);
				}

				throw new AntivirusScannerException(
					"Some error ocurred while trying to analyze file "
						+ filePath);
			}
		}
		catch (IOException ioe) {
			throw new SystemException("Unable to scan file", ioe);
		}
		catch (InterruptedException ie) {
			throw new SystemException("Unable to scan file", ie);
		}
		finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		EsetAntivirusScannerImpl.class);

}