/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.okhttp.ws;

import com.squareup.okhttp.Response;
import java.io.IOException;
import okio.Buffer;
import okio.BufferedSource;

import static com.squareup.okhttp.ws.WebSocket.PayloadType;

/** Listener for server-initiated messages on a connected {@link WebSocket}. */
public interface WebSocketListener {
  /**
   * Called when the request has successfully been upgraded to a web socket.
   */
  void onOpen(WebSocket webSocket, Response response) throws IOException;

  /**
   * Called when the transport or protocol layer of this web socket errors during communication.
   *
   * @param response Present when the failure is a direct result of the response (e.g., failed
   * upgrade, non-101 response code, etc.). {@code null} otherwise.
   */
  void onFailure(IOException e, Response response);

  /**
   * Called when a server message is received. The {@code type} indicates whether the
   * {@code payload} should be interpreted as UTF-8 text or binary data.
   *
   * <p>Implementations <strong>must</strong> call {@code source.close()} before returning. This
   * indicates completion of parsing the message payload and will consume any remaining bytes in
   * the message.
   */
  void onMessage(BufferedSource payload, PayloadType type) throws IOException;

  /**
   * Called when a server pong is received. This is usually a result of calling {@link
   * WebSocket#sendPing(Buffer)} but might also be unsolicited.
   */
  void onPong(Buffer payload);

  /**
   * Called when the server sends a close message. This may have been initiated
   * from a call to {@link WebSocket#close(int, String) close()} or as an unprompted
   * message from the server.
   *
   * @param code The <a href="http://tools.ietf.org/html/rfc6455#section-7.4.1">RFC-compliant</a>
   * status code.
   * @param reason Reason for close or an empty string.
   */
  void onClose(int code, String reason);
}
