/*
 * Copyright 2016 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package guru.mmp.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * The MemoryClassLoader class supports the loading of classes from set of in-memory JARs.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class MemoryClassLoader
  extends ClassLoader
{
  /**
   * The list of byte arrays containing the binary data representation of the JARs associated with
   * the MemoryClassLoader.
   */
  private List<byte[]> jars = new ArrayList<>();

  /**
   * Constructor
   *
   * @param jar an array of bytes giving the binary data representation of a JAR
   */
  public MemoryClassLoader(byte[] jar)
  {
    super();
    jars.add(jar);
  }

  /**
   * Constructor
   *
   * @param jar    an array of bytes giving the binary data representation of a JAR
   * @param parent the parent class loader
   */
  public MemoryClassLoader(byte[] jar, ClassLoader parent)
  {
    super(parent);
    jars.add(jar);
  }

  /**
   * Add a new JAR to the memory class loader.
   *
   * @param jar an array of bytes giving the binary data representation of a JAR
   */
  public void addJar(byte[] jar)
  {
    jars.add(jar);
  }

  /**
   * Clear the list of JARs associated with the memory class loader.
   */
  public void clearJars()
  {
    jars.clear();
  }

  /**
   * @param name the name of the class
   *
   * @return the resulting Class object
   *
   * @throws ClassNotFoundException
   * @see ClassLoader#findClass(String)
   */
  @Override
  public Class<?> findClass(String name)
    throws ClassNotFoundException
  {
    try
    {
      byte[] classData = getClassData(name);

      // If no class was found then delegate to super
      if (classData == null)
      {
        return super.findClass(name);
      }
      else
      {
        return defineClass(name, classData, 0, classData.length);
      }
    }
    catch (IOException e)
    {
      throw new ClassNotFoundException(
        "An IOException occurred while attempting to retrieve the" + " binary representation of" +
          " the class (" + name + ") from the JAR: " + e.getMessage());
    }
  }

  /**
   * @param name the resource name
   *
   * @return a URL object for reading the resource, or <code>null</code> if the resource could not
   * be found
   *
   * @see ClassLoader#getResource(String)
   */
  @Override
  public URL getResource(String name)
  {
    try
    {
      byte[] resourceData = getResourceData(name);

      if (resourceData == null)
      {
        return null;
      }

      return new URL("MemoryClassLoader", "", 0, name, new MemoryUrlStreamHandler(this));
    }
    catch (Throwable e)
    {
      throw new Error("Unable to create a URL for resource (" + name + "): " + e.getMessage());
    }
  }

  /**
   * Retrieve the binary data for the resource with the specified path.
   *
   * @param resourcePath the path to the resource
   *
   * @return the binary data for the resource with the specified path or <code>null</code> if the
   * resource could not be found
   *
   * @throws IOException an IO exception occurs
   */
  public byte[] getResourceData(String resourcePath)
    throws IOException
  {
    // Attempt to find the resource in every JAR associated with this class loader
    for (byte[] jarData : jars)
    {
      byte[] resourceData = getJarEntryAsBytes(resourcePath, jarData);

      if (resourceData != null)
      {
        return resourceData;
      }
    }

    return null;
  }

  /**
   * @see ClassLoader#findResources(String)
   */
  @Override
  protected Enumeration<URL> findResources(String name)
    throws IOException
  {
    throw new Error("The method MemoryClassLoader::findResources is not implemented");
  }

  /**
   * Retrieve the binary data for the class with the specified name.
   *
   * @param className the fully qualified name of the class to find
   *
   * @return the binary data for the class with the specified name or <code>null</code> if the
   * class could not be found
   *
   * @throws IOException an IO exception occurs
   */
  private byte[] getClassData(String className)
    throws IOException
  {
    // Build the path for the class file in the JAR
    String classPath = className.replace('.', '/');

    classPath += ".class";

    // Retrieve the binary representation of the each JAR associated with the
    // class loader and attempt to load the class from it
    for (byte[] jarData : jars)
    {
      byte[] classData = getJarEntryAsBytes(classPath, jarData);

      if (classData != null)
      {
        return classData;
      }
    }

    return null;
  }

  /**
   * Retrieve a JAR entry as a byte array.
   *
   * @param jarInputStream the JarInputStream pointing at the beginning of the JAR entry
   *
   * @return the JAR entry as a byte array
   *
   * @throws IOException an IO exception occurs
   */
  private byte[] getJarEntryAsBytes(JarInputStream jarInputStream)
    throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int noBytesRead;
    byte[] tmpBuffer = new byte[1024];

    while ((noBytesRead = jarInputStream.read(tmpBuffer)) != -1)
    {
      baos.write(tmpBuffer, 0, noBytesRead);
    }

    return baos.toByteArray();
  }

  /**
   * Retrieve the JAR entry with the specified path as an array of bytes from the JAR file given by
   * the specified binary data.
   * <p/>
   * If any jar/zip files are found they will be processed recursively.
   *
   * @param path    the path for the entry e.g. guru/mmp/common/util/MemoryClassLoader.class or
   *                guru/mmp/common/util/Test.properties
   * @param jarData the binary representation of the JAR file to search
   *
   * @return the binary representation of the JAR entry or <code>null</code> if the entry could not
   * be found
   *
   * @throws IOException an IO exception occurs
   */
  private byte[] getJarEntryAsBytes(String path, byte[] jarData)
    throws IOException
  {
    // Create the jar input stream for parsing
    JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(jarData));
    JarEntry entry;

    // Read each entry in the JAR file looking for the class with the specified path.
    // If a JAR or ZIP is found embedded in the JAR file then process it recursively
    while ((entry = jarInputStream.getNextJarEntry()) != null)
    {
      if (entry.getName().endsWith(".jar") || entry.getName().endsWith(".zip"))
      {
        byte[] classData = getJarEntryAsBytes(path, getJarEntryAsBytes(jarInputStream));

        if (classData != null)
        {
          return classData;
        }
      }
      else if (entry.getName().equals(path))
      {
        return getJarEntryAsBytes(jarInputStream);
      }
    }

    return null;
  }

  /**
   * The MemoryUrlConnection nested class provides a URL connection for a resource stored in a JAR
   * file managed by a MemoryClassLoader instance.
   */
  private class MemoryUrlConnection
    extends URLConnection
  {
    private MemoryClassLoader memoryClassLoader = null;

    /**
     * Constructs a MemoryUrlConnection using the specified URL and MemoryClassLoader.
     *
     * @param url               the specified URL
     * @param memoryClassLoader the memory class loader
     */
    public MemoryUrlConnection(URL url, MemoryClassLoader memoryClassLoader)
    {
      super(url);
      this.memoryClassLoader = memoryClassLoader;
    }

    /**
     * @see URLConnection#connect()
     */
    public void connect() {}

    /**
     * Returns an input stream that reads from this open connection.
     *
     * @return an input stream that reads from this open connection
     *
     * @throws IOException if an I/O error occurs while creating the input stream
     */
    @Override
    public InputStream getInputStream()
      throws IOException
    {
      byte[] resourceData = memoryClassLoader.getResourceData(getURL().getFile());

      if (resourceData == null)
      {
        throw new IOException(
          "Unable to find the resource (" + getURL().getFile() + ") in the in-memory JAR (" +
            getURL().getHost() + ")");
      }

      return new ByteArrayInputStream(resourceData);
    }
  }

  /**
   * The MemoryUrlStreamHandler nested class that provides URL stream handling capabilities for
   * resources stored in a JAR file managed by a MemoryClassLoader instance.
   */
  private class MemoryUrlStreamHandler
    extends URLStreamHandler
  {
    private MemoryClassLoader memoryClassLoader = null;

    /**
     * Constructs a MemoryUrlStreamHandler using the specified MemoryClassLoader.
     *
     * @param memoryClassLoader the memory class loader
     */
    public MemoryUrlStreamHandler(MemoryClassLoader memoryClassLoader)
    {
      this.memoryClassLoader = memoryClassLoader;
    }

    /**
     * Opens a connection to the object referenced by the URL argument.
     *
     * @param url the URL that this connects to
     *
     * @return a URLConnection object for the
     *
     * @see URLStreamHandler#openConnection(java.net.URL)
     */
    @Override
    public java.net.URLConnection openConnection(URL url)
    {
      return new MemoryUrlConnection(url, memoryClassLoader);
    }
  }
}
