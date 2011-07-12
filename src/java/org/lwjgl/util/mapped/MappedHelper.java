/*
 * Copyright (c) 2002-2011 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.util.mapped;

import java.nio.ByteBuffer;

/**
 * [INTERNAL USE ONLY]
 * <p/>
 * Helper class used by the bytecode transformer.
 *
 * @author Riven
 */
public class MappedHelper {

	public static void setup(MappedObject mo, ByteBuffer buffer, int align, int sizeof) {
		if ( mo.baseAddress != 0L )
			throw new IllegalStateException("this method should not be called by user-code");

		if ( buffer == null )
			throw new NullPointerException("buffer");
		if ( !buffer.isDirect() )
			throw new IllegalArgumentException("bytebuffer must be direct");
		mo.preventGC = buffer;

		if ( align <= 0 )
			throw new IllegalArgumentException("invalid alignment");
		mo.align = align;

		if ( sizeof % align != 0 )
			throw new IllegalStateException("sizeof not a multiple of alignment");
		mo.stride = sizeof;

		long addr = MappedObjectUnsafe.getBufferBaseAddress(buffer) + buffer.position();
		if ( addr % align != 0 )
			throw new IllegalStateException("buffer address not aligned on " + align + " bytes");

		mo.baseAddress = mo.viewAddress = addr;
	}

	public static void put_views(MappedSet2 set, int view) {
		set.view(view);
	}

	public static void put_views(MappedSet3 set, int view) {
		set.view(view);
	}

	public static void put_views(MappedSet4 set, int view) {
		set.view(view);
	}

	public static void put_view(MappedObject mapped, int view) {
		mapped.setViewAddress(mapped.baseAddress + view * mapped.stride);
	}

	public static int get_view(MappedObject mapped) {
		return (int)(mapped.viewAddress - mapped.baseAddress) / mapped.stride;
	}

	public static MappedObject dup(MappedObject src, MappedObject dst) {
		dst.baseAddress = src.baseAddress;
		dst.viewAddress = src.viewAddress;
		dst.stride = src.stride;
		dst.align = src.align;
		dst.preventGC = src.preventGC;
		return dst;
	}

	public static MappedObject slice(MappedObject src, MappedObject dst) {
		dst.baseAddress = src.viewAddress; // !
		dst.viewAddress = src.viewAddress;
		dst.stride = src.stride;
		dst.align = src.align;
		dst.preventGC = src.preventGC;
		return dst;
	}

	public static void copy(MappedObject src, MappedObject dst, int bytes) {
		MappedObjectUnsafe.INSTANCE.copyMemory(src.viewAddress, dst.viewAddress, bytes);
	}

	public static ByteBuffer newBuffer(long address, int capacity) {
		return MappedObjectUnsafe.newBuffer(address, capacity);
	}

	// ---- primitive fields read/write

	// byte

	public static void bput(byte value, long addr) {
		MappedObjectUnsafe.INSTANCE.putByte(addr, value);
	}

	public static byte bget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getByte(addr);
	}

	// short

	public static void sput(short value, long addr) {
		MappedObjectUnsafe.INSTANCE.putShort(addr, value);
	}

	public static short sget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getShort(addr);
	}

	// char

	public static void cput(char value, long addr) {
		MappedObjectUnsafe.INSTANCE.putChar(addr, value);
	}

	public static char cget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getChar(addr);
	}

	// int

	public static void iput(int value, long addr) {
		MappedObjectUnsafe.INSTANCE.putInt(addr, value);
	}

	public static int iget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getInt(addr);
	}

	// float

	public static void fput(float value, long addr) {
		MappedObjectUnsafe.INSTANCE.putFloat(addr, value);
	}

	public static float fget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getFloat(addr);
	}

	// long

	public static void jput(long value, long addr) {
		MappedObjectUnsafe.INSTANCE.putLong(addr, value);
	}

	public static long jget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getLong(addr);
	}

	// double

	public static void dput(double value, long addr) {
		MappedObjectUnsafe.INSTANCE.putDouble(addr, value);
	}

	public static double dget(long addr) {
		return MappedObjectUnsafe.INSTANCE.getDouble(addr);
	}

}