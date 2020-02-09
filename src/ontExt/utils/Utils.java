package ontExt.utils;

import java.io.File;
import java.util.Optional;

public class Utils
{

	public static Optional<File> getFile(String filepath)
	{
		File file = new File(filepath);
		if (file.exists() && file.isFile())
			return Optional.of(file);
		else
			return Optional.empty();
	}
}
