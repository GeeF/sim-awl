package org.pr2.simawl.client;

import java.io.IOException;

public class BufferedReader extends Reader {

  /** The next saved character. */
  private int savedNextChar;

  private Reader source;

  /**
   * Constructor.
   * 
   * @param source
   *            The source reader.
   */
  public BufferedReader(Reader source) {
      this.source = source;
      this.savedNextChar = -2;
  }

  /**
   * Constructor.
   * 
   * @param source
   *            The source reader.
   * @param size
   *            The size of the buffer.
   */
  public BufferedReader(Reader source, int size) {
      this.source = source;
      this.savedNextChar = -2;
  }
  
  /**
   * 
   */
  public void close() throws IOException {

  }

  /**
   * Returns the source reader.
   * 
   * @return The source reader.
   */
  private Reader getSource() {
      return source;
  }

  /**
   * Returns the next character, either the saved one or the next one from the
   * source reader.
   * 
   * @return The next character.
   * @throws IOException
   */
  public int read() throws IOException {
      int result = -1;

      if (this.savedNextChar != -2) {
          result = this.savedNextChar;
          this.savedNextChar = -2;
      } else {
          result = getSource().read();
      }

      return result;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
      return source.read(cbuf, off, len);
  }

  /**
   * Reads the next line of characters.
   * 
   * @return The next line.
   */
  public String readLine() throws IOException {
      StringBuilder sb = null;
      boolean eol = false;
      int nextChar = read();

      while (!eol && (nextChar != -1)) {
          if (nextChar == 10) {
              eol = true;
          } else if (nextChar == 13) {
              eol = true;

              // Check if there is a immediate LF following the CR
              nextChar = read();
              if (nextChar != 10) {
                  this.savedNextChar = nextChar;
              }
          }

          if (!eol) {
              if (sb == null) {
                  sb = new StringBuilder();
              }

              sb.append((char) nextChar);
              nextChar = read();
          }

      }

      return (sb == null) ? null : sb.toString();
  }
}
