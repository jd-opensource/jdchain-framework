package com.jd.blockchain.contract.archiver;

import java.io.File;

class ContractFile implements Archive {

		private String type;

		private ArchiveLayout layout;

		private File file;

		public ContractFile(String type, ArchiveLayout layout, File file) {
			this.type = type;
			this.layout = layout;
			this.file = file;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public ArchiveLayout getLayout() {
			return layout;
		}

		@Override
		public File getFile() {
			return file;
		}

	}