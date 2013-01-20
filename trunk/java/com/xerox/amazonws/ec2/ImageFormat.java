//
// typica - A client library for Amazon Web Services
// Copyright (C) 2011 Eucalyptus Systems
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.xerox.amazonws.ec2;

/**
 * This enumeration represents different image format that can be imported.
 */
public enum ImageFormat {
	VMDK ("VMDK"),
	RAW ("RAW");

	private final String formatId;

	ImageFormat(String formatId) {
		this.formatId = formatId;
	}

	public String getFormatId() {
		return formatId;
	}

	public static ImageFormat getFormatFromString(String val) {
		for (ImageFormat t : ImageFormat.values()) {
			if (t.getFormatId().equals(val)) {
				return t;
			}
		}
		return null;
	}
}
