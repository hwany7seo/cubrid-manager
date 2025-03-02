/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search Solution.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of the <ORGANIZATION> nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */
package com.cubrid.jdbc.proxy.driver;

/**
 * CUBRID Isolation level class.
 *
 * @author pangqiren
 * @version 1.0 - 2009-12-28 created by pangqiren
 */
public class CUBRIDIsolationLevel {
    public static final int TRAN_MIN = 1;
    public static final int TRAN_MAX = 6;
    public static final int TRAN_UNKNOWN_ISOLATION = 0;
    public static final int TRAN_COMMIT_CLASS_UNCOMMIT_INSTANCE = 1;
    public static final int TRAN_COMMIT_CLASS_COMMIT_INSTANCE = 2;
    /* TRAN_READ_UNCOMMITTED */
    public static final int TRAN_REP_CLASS_UNCOMMIT_INSTANCE = 3;
    /* TRAN_READ_COMMITTED */
    public static final int TRAN_REP_CLASS_COMMIT_INSTANCE = 4;
    /* TRAN_SERIALIZABLE */
    public static final int TRAN_REP_CLASS_REP_INSTANCE = 5;
    public static final int TRAN_SERIALIZABLE = 6;
}
